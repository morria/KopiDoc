package com.etsy.kopiDoc.source;

import com.etsy.kopiDoc.source.lucene.RootDocDocumentFactory;
import com.sun.javadoc.RootDoc;
import java.io.File;
import java.io.IOException;
import java.lang.Thread;
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
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
  * Manage source files by scanning and monitoring for changes
  * @author Andrew Morrison <asm@etsy.com>
  */
public class SourceManager
{
  private static Logger logger = Logger.getLogger(SourceManager.class.getName());

  private HashMap<String,RootDoc> fileToRootDocMap = null;
  private HashMap<String,Integer> directoryToWatchMap = null;

  private QueryParser queryParser = null;
  private IndexWriter indexWriter = null;
  private Directory indexDirectory = null;

  /**
    *
    */
  public SourceManager()
  {
    fileToRootDocMap = new HashMap<String,RootDoc>(50);
    directoryToWatchMap = new HashMap<String, Integer>(30);

    indexDirectory = new RAMDirectory();
    KeywordAnalyzer analyzer = new KeywordAnalyzer();
    queryParser = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer);

    try {
      indexWriter = new IndexWriter(indexDirectory,
                                    analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
      indexWriter.commit();
    }
    catch (Exception e) {
      logger.error(e.toString());
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
      return false;
    }

    for(File file : fileSet)
      addSource(file, sourcePath, classPath);

    return true;
  }

  /**
    *
    */
  public boolean addSource(File file, String sourcePath, String classPath)
  {
    removeSource(file);

    Javadoc javadoc = new Javadoc();
    javadoc.execute(file.getAbsolutePath(), sourcePath, classPath);
    RootDoc rootDoc = javadoc.getRootDoc();

    logger.debug("Adding document with path " + file.getAbsolutePath());
    fileToRootDocMap.put(file.getAbsolutePath(), rootDoc);

    Document document =
      RootDocDocumentFactory.getDocument(file, sourcePath, rootDoc);

    try {
      indexWriter.addDocument(document);
      indexWriter.commit();
    }
    catch (Exception e) {
      logger.error(e.toString());
      return false;
    }

    try
    {
      String parentDirectoryName = file.getParentFile().getAbsolutePath();
      if(directoryToWatchMap.get(parentDirectoryName) == null)
      {
        logger.debug("Adding watch");
        int watchId = 
          JNotify.addWatch(parentDirectoryName, JNotify.FILE_ANY, 
                           false, new Listener(sourcePath, classPath));
        directoryToWatchMap.put(parentDirectoryName, watchId);
      }
    }
    catch(JNotifyException e)
    {
      logger.error(e.toString());
      return false;
    }

    return true;
  }

  /**
    * 
    */
  public boolean removeSource(File file)
  {
    logger.debug("Remove File " + file.toString());
    return false;
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
      IndexSearcher indexSearcher = new IndexSearcher(indexDirectory, true);
      for(ScoreDoc hit : indexSearcher.search(query, 1000).scoreDocs)
        documentList.add(indexSearcher.doc(hit.doc));
    }
    catch(Exception e) {
      logger.error(e.toString());
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
      IndexSearcher indexSearcher = new IndexSearcher(indexDirectory, true);
      for(ScoreDoc hit : indexSearcher.search(query, 1).scoreDocs)
        return indexSearcher.doc(hit.doc);
    }
    catch(Exception e) {
      logger.error(e.toString());
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
      logger.info("renamed " + rootPath + " : " + oldName + " -> " + newName);
      removeSource(new File(rootPath, oldName));
      addSource(new File(rootPath, newName), sourcePath, classPath);
    }


    public void fileModified(int wd, String rootPath, String name) {
      logger.info("modified " + rootPath + " : " + name);
      addSource(new File(rootPath, name), sourcePath, classPath);
    }

    public void fileDeleted(int wd, String rootPath, String name) {
      logger.info("deleted " + rootPath + " : " + name);
      removeSource(new File(rootPath, name));
    }

    public void fileCreated(int wd, String rootPath, String name) {
      logger.info("created " + rootPath + " : " + name);
      addSource(new File(rootPath, name), sourcePath, classPath);
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
      super(HiddenFileFilter.VISIBLE, FileFilterUtils.suffixFileFilter("java"), 15);
      logger.debug("Walking " + startDirectory.toString());
      walk(startDirectory, results);
    }

    public void handleFile(File file, int depth, Collection results)
    {
      results.add(file);
    }
  }
}
