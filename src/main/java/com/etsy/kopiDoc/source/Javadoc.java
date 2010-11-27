package com.etsy.kopiDoc.source;

import com.sun.javadoc.*;
import com.sun.tools.javadoc.*;
import org.apache.log4j.Logger;

public class Javadoc
{
  private static Logger logger = Logger.getLogger(Javadoc.class.getName());

  /**
    * Doclets have no reasonable mechanism for returning up the stack, so
    * instead we short-circuit and store the docs here, hoping they don't
    * get overwritten.
    */
  public static RootDoc lastRootDoc = null;

  /**
   * 
   */
  public Javadoc() { }

  public RootDoc getRootDoc() { return lastRootDoc; };
  public static void setRootDoc(RootDoc rootDoc)  { lastRootDoc = rootDoc;  }

  /**
    *
    */
  public void execute(String fileName, String sourcePath, String classPath)
  {
    String[] args = {"-sourcepath", sourcePath,
                     "-classpath", classPath, fileName};

    Main.execute("SourceSearchServer", KopiDoclet.class.getName(), args);
  }

}