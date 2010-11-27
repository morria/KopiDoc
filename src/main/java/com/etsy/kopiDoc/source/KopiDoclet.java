package com.etsy.kopiDoc.source;

import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import org.apache.log4j.Logger;

/**
  * Transform a document into documentation
  *
  * @author Andrew S. Morrison <asm@etsy.com>
  */
public class KopiDoclet
  extends Doclet
{
  private static Logger logger = Logger.getLogger(KopiDoclet.class.getName());

  /**
   * @inheritDoc
   */
  public static boolean start(RootDoc rootDoc)
  {
    Javadoc.setRootDoc(rootDoc);
    return true;
  }
}