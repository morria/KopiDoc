package io.adroit.kopiDoc.source;

import java.io.IOException;
import java.util.LinkedList;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
  * Serialize a Javadoc RootDoc into JSON
  */
public class JsonRootDocSerializer
  extends JsonSerializer<RootDoc>
{
  private static Logger logger = Logger.getLogger(JsonRootDocSerializer.class.getName());

  public void serialize(RootDoc rootDoc, JsonGenerator gen, SerializerProvider provider)
    throws IOException, JsonProcessingException 
  {
    gen.writeStartObject();

    ClassDoc[] classDocSet = rootDoc.specifiedClasses();

    if(classDocSet.length < 1)
    {
      logger.debug("Document contains no class");
      gen.writeEndObject();
      return;
    }

    ClassDoc classDoc = classDocSet[0];

    // Class Name
    gen.writeStringField("className",classDoc.qualifiedName());

    // Comments
    gen.writeStringField("comment",classDoc.commentText());
    for(Tag tag : classDoc.tags())
      gen.writeStringField(tag.kind(), tag.text());

    // Class Qualifiers
    gen.writeBooleanField("isAbstract", classDoc.isAbstract());
    gen.writeBooleanField("isExternalizable", classDoc.isExternalizable());
    gen.writeBooleanField("isSerializable", classDoc.isSerializable());
    
    // Super-Class
    if(classDoc.superclass() != null)
    {
      gen.writeFieldName("superclassType");
      serializeType(classDoc.superclassType(),gen, provider);
    }

    /*
    // Imported Classes
    gen.writeFieldName("importedClasses");
    gen.writeStartArray();
    if(classDoc.importedClasses() != null)
      for(ClassDoc importedClassDoc : classDoc.importedClasses())
        if(importedClassDoc.qualifiedName() != null)
          gen.writeString(importedClassDoc.qualifiedName());
    gen.writeEndArray();
    */

    // Interfaces Implemented
    if(classDoc.interfaceTypes() != null)
    {
      gen.writeFieldName("interfaceTypes");
      gen.writeStartArray();
      for(Type interfaceType : classDoc.interfaceTypes())
        serializeType(interfaceType, gen, provider);
      gen.writeEndArray();
    }

    // Fields
    gen.writeFieldName("fields");
    gen.writeStartArray();
    if(classDoc.fields() != null)
      for(FieldDoc field : classDoc.fields())
        serializeField(field, gen, provider);
    gen.writeEndArray();

    // Constructors

    // Methods
    gen.writeFieldName("methods");
    gen.writeStartArray();
    if(classDoc.methods() != null)
      for(MethodDoc method : classDoc.methods())
        serializeMethod(method, gen, provider);
    gen.writeEndArray();


    // Inner-Classes

    gen.writeEndObject();
  }


  public void serializeType(Type type, JsonGenerator gen, SerializerProvider provider)
    throws IOException, JsonProcessingException 
  {
    gen.writeStartObject();
    {
      gen.writeStringField("qualifiedName", type.qualifiedTypeName());
      gen.writeStringField("simpleTypeName", type.simpleTypeName());
      gen.writeStringField("dimension", type.dimension());
      gen.writeBooleanField("isPrimitive", type.isPrimitive());
    }
    gen.writeEndObject();
  }

  public void serializeParameter(Parameter parameter, JsonGenerator gen, SerializerProvider provider)
    throws IOException, JsonProcessingException 
  {
    gen.writeStartObject();
    {
      gen.writeStringField("name", parameter.name());
      gen.writeFieldName("type");
      serializeType(parameter.type(), gen, provider);
    }
    gen.writeEndObject();
  }


  public void serializeMethod(MethodDoc method, JsonGenerator gen, SerializerProvider provider)
    throws IOException, JsonProcessingException 
  {
    gen.writeStartObject();
    {
      // Method Name
      gen.writeStringField("name", method.qualifiedName());

      // Comments
      gen.writeStringField("comment", method.commentText());

      for(Tag tag : method.tags())
        gen.writeStringField(tag.kind(), tag.text());

      // Method Overridea
      if(method.overriddenType() != null)
        gen.writeStringField("overrides", method.overriddenType().qualifiedTypeName());

      // Parameters
      gen.writeFieldName("parameters");
      gen.writeStartArray();
      for(Parameter parameter : method.parameters())
        serializeParameter(parameter, gen, provider);
      gen.writeEndArray();

      // Thrown Exceptions
      gen.writeFieldName("thrownExceptions");
      gen.writeStartArray();
      for(Type type : method.thrownExceptionTypes())
        serializeType(type, gen, provider);
      gen.writeEndArray();

      // Qualifiers
      gen.writeBooleanField("isAbstract", method.isAbstract());
      gen.writeBooleanField("isStatic", method.isStatic());
      gen.writeBooleanField("isFinal", method.isFinal());
      gen.writeBooleanField("isPublic", method.isPublic());
      gen.writeBooleanField("isPrivate", method.isPrivate());
      gen.writeBooleanField("isProtected", method.isProtected());
      gen.writeBooleanField("isNative", method.isNative());
      gen.writeBooleanField("isVarArgs", method.isVarArgs());
      gen.writeBooleanField("isSynthetic", method.isSynthetic());

      // Signature
      gen.writeStringField("signature", method.signature());

      // Return Type
      gen.writeFieldName("returnType");
      serializeType(method.returnType(), gen, provider);
    }
    gen.writeEndObject();
  }
 
  public void serializeField(FieldDoc field, JsonGenerator gen, SerializerProvider provider)
    throws IOException, JsonProcessingException 
  {
    gen.writeStartObject();
    {
      // Name
      gen.writeStringField("name", field.qualifiedName());

      // Comments
      gen.writeStringField("comment", field.commentText());
      for(Tag tag : field.tags())
        gen.writeStringField(tag.kind(), tag.text());
      
      // Values
      gen.writeObjectField("constantValue", field.constantValue());
      gen.writeStringField("constantValueExpression", field.constantValueExpression());

      // Qualifiers
      gen.writeBooleanField("isTransient", field.isTransient());
      gen.writeBooleanField("isVolatile", field.isVolatile());
      gen.writeBooleanField("isSynthetic", field.isSynthetic());
      gen.writeBooleanField("isStatic", field.isStatic());
      gen.writeBooleanField("isFinal", field.isFinal());
      gen.writeBooleanField("isPublic", field.isPublic());
      gen.writeBooleanField("isPrivate", field.isPrivate());
      gen.writeBooleanField("isProtected", field.isProtected());

      // Type
      gen.writeFieldName("type");
      serializeType(field.type(), gen, provider);
    }
    gen.writeEndObject();
  } 
}
