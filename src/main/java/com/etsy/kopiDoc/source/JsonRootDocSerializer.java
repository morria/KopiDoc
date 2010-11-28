package com.etsy.kopiDoc.source;

import java.io.IOException;
import java.util.LinkedList;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class JsonRootDocSerializer
  extends JsonSerializer<RootDoc>
{
  public void serialize(RootDoc rootDoc, JsonGenerator gen, SerializerProvider provider)
    throws IOException, JsonProcessingException 
  {
    ClassDoc[] classDocSet = rootDoc.specifiedClasses();

    if(classDocSet.length < 1)
      return;

    ClassDoc classDoc = classDocSet[0];

    gen.writeStartObject();
    {
      gen.writeStringField("className",classDoc.qualifiedName());
      gen.writeStringField("comment",classDoc.commentText());

      for(Tag tag : classDoc.tags())
        gen.writeStringField(tag.kind(), tag.text());

      gen.writeFieldName("importedClasses");
      gen.writeStartArray();
      for(ClassDoc importedClassDoc : classDoc.importedClasses())
        gen.writeString(importedClassDoc.qualifiedName());
      gen.writeEndArray();

      gen.writeFieldName("interfaces");
      gen.writeStartArray();
      for(ClassDoc interfaceClassDoc : classDoc.interfaces())
        gen.writeString(interfaceClassDoc.qualifiedName());
      gen.writeEndArray();

      gen.writeBooleanField("isAbstract", classDoc.isAbstract());
      gen.writeBooleanField("isExternalizable", classDoc.isExternalizable());
      gen.writeBooleanField("isSerializable", classDoc.isSerializable());

      gen.writeFieldName("fields");
      gen.writeStartArray();
      for(FieldDoc field : classDoc.fields())
      {
        gen.writeStartObject();
        {
          gen.writeStringField("name", field.qualifiedName());
          gen.writeStringField("comment", field.commentText());
          for(Tag tag : field.tags())
            gen.writeStringField(tag.kind(), tag.text());

          gen.writeObjectField("constantValue", field.constantValue());
          gen.writeStringField("constantValueExpression", field.constantValueExpression());
          gen.writeBooleanField("isTransient", field.isTransient());
          gen.writeBooleanField("isVolatile", field.isVolatile());
          gen.writeStringField("type", field.type().typeName());
        }
        gen.writeEndObject();
      }
      gen.writeEndArray();

      gen.writeFieldName("methods");
      gen.writeStartArray();
      for(MethodDoc method : classDoc.methods())
      {
        gen.writeStartObject();
        {
          gen.writeStringField("name", method.qualifiedName());
          gen.writeStringField("comment", method.commentText());

          for(Tag tag : method.tags())
            gen.writeStringField(tag.kind(), tag.text());

          gen.writeFieldName("parameters");
          gen.writeStartArray();
          for(Parameter parameter : method.parameters())
          {
            gen.writeStartObject();
            {
              gen.writeStringField("name", parameter.name());
              gen.writeStringField("type", parameter.typeName());
            }
            gen.writeEndObject();
          }
          gen.writeEndArray();


          gen.writeBooleanField("isAbstract", method.isAbstract());
          gen.writeBooleanField("isPublic", method.isPublic());
          gen.writeBooleanField("isPrivate", method.isPrivate());
          gen.writeBooleanField("isProtected", method.isProtected());
          gen.writeStringField("returnType", method.returnType().typeName());
          if(method.overriddenMethod() != null)
            gen.writeStringField("overriddenMethodName", method.overriddenMethod().qualifiedName());
        }
        gen.writeEndObject();
      }
      gen.writeEndArray();




    }
    gen.writeEndObject();
  }
}