package com.etsy.kopiDoc.source;

import com.sun.javadoc.RootDoc;
import java.io.File;
import java.io.IOException;
import java.lang.Thread;
import java.util.Collection;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyListener;
import net.contentobjects.jnotify.JNotifyException;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.log4j.Logger;

/**
  * Manage source files by scanning and monitoring for changes
  * @author Andrew Morrison <asm@etsy.com>
  */
public class SourceManager
{
  private static Logger logger = Logger.getLogger(SourceManager.class.getName());

  private HashMap<String,RootDoc> fileToRootDocMap = null;

  /**
    *
    */
  public SourceManager()
  {
    fileToRootDocMap = new HashMap<String,RootDoc>(50);
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

    try
    {
      int watchId = 
        JNotify.addWatch(file.getAbsolutePath(), 
                         JNotify.FILE_MODIFIED | JNotify.FILE_DELETED | JNotify.FILE_RENAMED,
                         true, new Listener(sourcePath, classPath));
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
  public RootDoc getDocByFileName(String absolutePath)
  {
    return fileToRootDocMap.get(absolutePath);
  }

  public Set getDocList()
  {
    return fileToRootDocMap.keySet();
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
