package io.adroit.kopiDoc.source.lucene;

import io.adroit.kopiDoc.source.JsonRootDocSerializer;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;

/**
  * Create a document for lucene out of a Javadoc document
  * @author Andrew S. Morrison <asm@etsy.com>
  */
public class RootDocDocumentFactory
{
  private static Logger logger = Logger.getLogger(RootDocDocumentFactory.class.getName());

  private RootDocDocumentFactory() { }

  /**
    * 
    */
  public static Document getDocument(File file, String sourcePath, 
                                     RootDoc rootDoc, String errorMessages)
  {
    String jsonDoc = getJsonFromRootDoc(rootDoc);

    ClassDoc[] classDocSet = rootDoc.specifiedClasses();

    if(classDocSet.length < 1) return null;

    ClassDoc classDoc = classDocSet[0];

    Document document = new Document();

    document.add(new Field("fileName", file.getAbsolutePath(),
                           Field.Store.YES, Field.Index.NOT_ANALYZED));

    document.add(new Field("className", classDoc.qualifiedName(),
                           Field.Store.YES, Field.Index.NOT_ANALYZED));

    document.add(new Field("lastModified", String.valueOf(file.lastModified()),
                           Field.Store.YES, Field.Index.NOT_ANALYZED));

    document.add(new Field("document", jsonDoc, 
                           Field.Store.YES, Field.Index.NO));

    document.add(new Field("errors", jsonDoc, 
                           Field.Store.YES, Field.Index.NO));

    return document;
  }


  private static String getJsonFromRootDoc(RootDoc rootDoc)
  {
    CustomSerializerFactory serializer = new CustomSerializerFactory();
    serializer.addGenericMapping(RootDoc.class, new JsonRootDocSerializer());
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializerFactory(serializer);

    try {
      return mapper.writeValueAsString(rootDoc); 
    }
    catch(IOException e)
    {
      logger.error(e.toString());
      return null;
    }
  }

}
