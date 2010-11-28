package io.adroit.kopiDoc.source;

import io.adroit.kopiDoc.source.lucene.RootDocDocumentFactory;
import com.sun.javadoc.RootDoc;
import java.io.File;
import java.io.IOException;
import java.lang.Thread;
import java.lang.Runtime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;
import java.util.Collection;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyListener;
import net.contentobjects.jnotify.JNotifyException;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
  * Manage source files by scanning and monitoring for changes as well as
  * managing the Lucene index
  * @author Andrew Morrison <asm@etsy.com>
  */
public class SourceManager
{
  private static Logger logger = Logger.getLogger(SourceManager.class.getName());

  private HashMap<String,Integer> directoryToWatchMap = null;

  private QueryParser queryParser = null;
  private IndexWriter indexWriter = null;
  private Directory indexDirectory = null;
  private IndexSearcher indexSearcher = null;

  /**
    *
    */
  public SourceManager()
  {
    directoryToWatchMap = new HashMap<String, Integer>(30);

    KeywordAnalyzer analyzer = new KeywordAnalyzer();

    try {
      indexDirectory = getIndexDirectory();
      indexWriter = new IndexWriter(indexDirectory,
                                    analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);

      queryParser = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer);
    }
    catch (Exception e) {
      logger.error(e.toString());
      Thread.currentThread().dumpStack();
    }

    Runtime.getRuntime().addShutdownHook(new ShutdownHook());
  }

  /**
    * 
    */
  private IndexSearcher getCurrentIndexSearcher()
  {
    try {
      // If none exits, create it
      if(indexSearcher == null) {
        indexSearcher = new IndexSearcher(IndexReader.open(indexDirectory,true));
        return indexSearcher;
      }

      // If it exists and is current, use it
      if(indexSearcher.getIndexReader().isCurrent())
      {
        return indexSearcher;
      }

      // If its out of date, reopen it
      logger.debug("Reopening Stale IndexSearcher");
      indexSearcher = new IndexSearcher(indexSearcher.getIndexReader().reopen(true));
      return indexSearcher;

    } catch(Exception e) {
      logger.error(e.toString());
      Thread.currentThread().dumpStack();
      return null;
    }
  }

  private Directory getIndexDirectory()
  {
    try
    {
      File directory = new File(System.getProperty("java.io.tmpdir"), "KopiDocStore");
      if(directory.exists())
      {
        if(!directory.isDirectory())
          return null;
      }
      else
        directory.mkdir();
      
      Directory indexDirectory = FSDirectory.open(directory);
      
      return indexDirectory;
    }
    catch(Exception e)
    {
      logger.error(e.toString());
      Thread.currentThread().dumpStack();
      return null;
    }
  }

  /**
    *
    */
  public boolean addSources(String sourcePath, String classPath)
  {
    String[] paths = sourcePath.split(":");
    LinkedList<File> fileSet = new LinkedList<File>();

    try 
    {
      for(String path : paths)
        new SourceManager.SourceWalker(new File(path),fileSet);
    }
    catch(IOException e)
    {
      logger.error(e.toString());
      Thread.currentThread().dumpStack();
      return false;
    }

    for(File file : fileSet)
      addSource(file, sourcePath, classPath);

    return true;
  }



  /**
    *
    */
  protected boolean addSource(File file, String sourcePath, String classPath)
  {
    Document document = 
      getDocumentByQueryString("fileName:" + file.getAbsolutePath());

    // If this doc is indexed, either kill it so we can re-index if the
    // incoming file is newer, or else don't re-index
    if(document != null) {
      if(file.lastModified() <= Long.parseLong(document.get("lastModified")))
      {
        logger.debug("addSource("+file.getAbsolutePath()+"): Index is current");
        return true;
      }
      logger.debug("addSource("+file.getAbsolutePath()+"): Removing stale file");
      removeDocumentFromIndexByFile(file);
    }
    else
      logger.debug("addSource("+file.getAbsolutePath()+"): Adding new file");

    Javadoc javadoc = new Javadoc();
    String errorMessages = javadoc.execute(file.getAbsolutePath(), sourcePath, classPath);
    RootDoc rootDoc = javadoc.getRootDoc();

    document =
      RootDocDocumentFactory.getDocument(file, sourcePath, rootDoc, errorMessages);

    boolean success = 
      addDocumentToIndex(document);

    if(!success)
      return false;

    try
    {
      String parentDirectoryName = file.getParentFile().getAbsolutePath();
      if(directoryToWatchMap.get(parentDirectoryName) == null)
      {
        int watchId = 
          JNotify.addWatch(parentDirectoryName, JNotify.FILE_ANY, 
                           false, new Listener(sourcePath, classPath));
        directoryToWatchMap.put(parentDirectoryName, watchId);
      }
    }
    catch(JNotifyException e)
    {
      logger.error(e.toString());
      Thread.currentThread().dumpStack();
      return false;
    }

    return true;
  }


  /**
   *
   */
  protected boolean addDocumentToIndex(Document document)
  {
    synchronized(this) {
      try {
        indexWriter.addDocument(document);
        indexWriter.commit();
      }
      catch (Exception e) {
        logger.error(e.toString());
        Thread.currentThread().dumpStack();
        return false;
      }
      return true;
    }
  }

  /**
    * Remove a file from the index
    *
    * @param file
    * A file which is to be removed from the index
    */
  protected boolean removeDocumentFromIndexByFile(File file)
  {
    synchronized(this) {
      logger.debug("removeDocumentFromIndexByFile("+file.getAbsolutePath()+")");
      try
      {
        Term term = new Term("fileName", file.getAbsolutePath());
        indexWriter.deleteDocuments(term);
        indexWriter.commit();
        indexWriter.expungeDeletes(true);
        return true;
      }
      catch(Exception e)
      {
        logger.error(e.toString());
        Thread.currentThread().dumpStack();
        return false;
      }
    }
  }

  /**
    * Get a collection of documents given a query
    *
    * @param query
    * Any lucene query
    *
    * @return
    * A collection of matched documents
    */
  protected Collection<Document> getDocumentCollectionByQuery(Query query)
  {
    LinkedList<Document> documentList = 
      new LinkedList<Document>();

    try
    {
      IndexSearcher searcher = getCurrentIndexSearcher();

      for(ScoreDoc hit : searcher.search(query, 1000).scoreDocs)
        documentList.add(searcher.doc(hit.doc));
    }
    catch(Exception e) {
      logger.error(e.toString());
      Thread.currentThread().dumpStack();
    }

    return documentList;
  }

  /**
    *
    */
  protected Collection<Document> getDocumentCollectionByQueryString(String queryString)
  {
    try
    {
      Query query = queryParser.parse(queryString);
      return getDocumentCollectionByQuery(query);
    }
    catch(ParseException e)
    {
      logger.error(e.toString());
      Thread.currentThread().dumpStack();
      return null;
    }
  }

  /**
    *  Get a single document given a query
    *
    * @param query
    * Any lucene query
    *
    * @return
    * A single (highest scoring) document if matched, else null
    */
  protected Document getDocumentByQuery(Query query)
  {
    try
    {
      IndexSearcher searcher = getCurrentIndexSearcher();

      for(ScoreDoc hit : searcher.search(query, 1).scoreDocs)
        return indexSearcher.doc(hit.doc);
    }
    catch(Exception e) {
      logger.error(e.toString());
      Thread.currentThread().dumpStack();
    }

    return null;
  }

  /**
    * 
    */
  protected Document getDocumentByQueryString(String queryString)
  {
    try
    {
      Query query = queryParser.parse(queryString);
      return getDocumentByQuery(query);
    }
    catch(ParseException e)
    {
      logger.error(e.toString());
      Thread.currentThread().dumpStack();
      return null;
    }
  }

  /**
    * Get a JSON document given an absolute file name
    *
    * @param absolutePath
    * The absolute path to a source file
    *
    * @return
    * A JSON document encoded as String
    */
  public String getJSONDocumentByFileName(String absolutePath)
  {
    Document document = getDocumentByQueryString("fileName:" + absolutePath);
    return document.get("document");
  }

  /**
    * Get a Collection of all file names in the index
    *
    * @return
    * A collection of all file names that are indexed
    */
  public Collection<String> getFileList()
  {
    LinkedList<String> fileList = new LinkedList<String>();
    Collection<Document> documents = getDocumentCollectionByQueryString("*:*");
    for(Document document : documents)
      fileList.add(document.get("fileName"));
    return fileList;
  }

  /**
    * Get a JSON document given a qualified class name
    *
    * @param qualifiedClassName
    * A fully qualified class name
    *
    * @return
    * A JSON document encoded as String
    */
  public String getJSONDocumentByClassName(String qualifiedClassName)
  {
    Document document = 
      getDocumentByQueryString("className:" + qualifiedClassName);
    return document.get("document");
  }

  /**
    * Get a Collection of class names in the index
    *
    * @return
    * A collection of class names in the index
    */
  public Collection<String> getClassList()
  {
    LinkedList<String> classList = new LinkedList<String>();

    Collection<Document> documents = getDocumentCollectionByQueryString("*:*");

    for(Document document : documents)
      classList.add(document.get("className"));

    return classList;
  }

  /*
  private void commit() {
    try {
      indexWriter.commit();
    } catch (Exception e) {
      logger.error("Commit Failed: " + e.toString());
      Thread.currentThread().dumpStack();
    }
  }
  */


  /**
    *
    */
  class Listener implements JNotifyListener
  {
    private String sourcePath;
    private String classPath;

    public Listener(String sourcePath, String classPath) {
      super();
      this.sourcePath = sourcePath;
      this.classPath = classPath;
    }

    public void fileRenamed(int wd, String rootPath, String oldName,
                            String newName) {
      File fileOld = new File(rootPath, oldName);
      if(HiddenFileFilter.VISIBLE.accept(fileOld))
      {
        logger.info("rename del " + fileOld.getAbsolutePath());
        removeDocumentFromIndexByFile(fileOld);
        // commit();
      }
      File fileNew = new File(rootPath, newName);
      if(HiddenFileFilter.VISIBLE.accept(fileNew))
      {
        logger.info("rename add " + fileNew.getAbsolutePath());
        addSource(fileNew, sourcePath, classPath);
        // commit();
      }
    }

    public void fileModified(int wd, String rootPath, String name) {
      File file = new File(rootPath, name);

      if(!HiddenFileFilter.VISIBLE.accept(file))
        return;

      logger.info("modified " + file.getAbsolutePath());
      addSource(file, sourcePath, classPath);
    }

    public void fileDeleted(int wd, String rootPath, String name) {
      File file = new File(rootPath, name);

      if(!HiddenFileFilter.VISIBLE.accept(file))
        return;

      logger.info("deleted " + file.getAbsolutePath());
      removeDocumentFromIndexByFile(file);
    }

    public void fileCreated(int wd, String rootPath, String name) {
      File file = new File(rootPath, name);

      if(!HiddenFileFilter.VISIBLE.accept(file))
        return;

      logger.info("created " + file.getAbsolutePath());
      addSource(file, sourcePath, classPath);
    }
  }

  class ShutdownHook extends Thread
  {
    public ShutdownHook() { }
    
    public void run() {
      logger.info("Committing and closing index on shutdown");
      try
      {
        synchronized(this) {
          indexWriter.commit();
          indexWriter.close();
        }
      } catch(Exception e) {
        logger.error(e);
        Thread.currentThread().dumpStack();
      }
    }
  }

  /**
    * 
    */
  class SourceWalker extends DirectoryWalker
  {
    public SourceWalker(File startDirectory, Collection results)
      throws IOException
    { 
      super(HiddenFileFilter.VISIBLE, 
            FileFilterUtils.andFileFilter(FileFilterUtils.suffixFileFilter("java"),
                                          HiddenFileFilter.VISIBLE), 15);

      walk(startDirectory, results);
    }

    public void handleFile(File file, int depth, Collection results)
    {
      results.add(file);
    }
  }
}
