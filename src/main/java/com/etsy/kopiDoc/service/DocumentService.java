package com.etsy.kopiDoc.service;

import com.etsy.kopiDoc.source.SourceManager;
import com.etsy.kopiDoc.source.JsonRootDocSerializer;
import com.sun.javadoc.RootDoc;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;


public class DocumentService extends AbstractService
{
  private static Logger logger = Logger.getLogger(DocumentService.class.getName());

  private static SourceManager sourceManager = null;

    public DocumentService(BayeuxServer bayeux)
    {
        super(bayeux, "document");
        sourceManager = new SourceManager();

        addService("/service/addSources", "addSources");
        addService("/service/getDocument", "getDocument");
        addService("/service/getDocumentList", "getDocumentList");

/*
        addService("/service/getClassList", "getClassList");
        addService("/service/getClass", "getClass");
*/

    }

    public void addSources(ServerSession remote, Message message)
    {
        Map<String, Object> input = message.getDataAsMap();
        String sourcePath = (String)input.get("sourcePath");
        String classPath = (String)input.get("classPath");

        boolean success =
          sourceManager.addSources(sourcePath,classPath);

        logger.debug("Sending success " + (success? "true":"false"));

        Map<String, Object> output = new HashMap<String, Object>();
        output.put("success", success? "true":"false");
        remote.deliver(getServerSession(), "/addSources", output, null);
    }


  public void getDocument(ServerSession remote, Message message)
  {
        Map<String, Object> input = message.getDataAsMap();
        String absolutePath = (String)input.get("absolutePath");

        RootDoc rootDoc = 
          sourceManager.getDocByFileName(absolutePath);

        CustomSerializerFactory serializer = new CustomSerializerFactory();
        serializer.addGenericMapping(RootDoc.class, new JsonRootDocSerializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializerFactory(serializer);

        Map<String, Object> output = new HashMap<String, Object>();
        try {
          String rootDocJson = mapper.writeValueAsString(rootDoc); 

          logger.debug(rootDocJson);

          output.put("class", rootDocJson);
        }
        catch(IOException e)
        {
          logger.error(e.toString());
          output.put("class", "fail");
        }

        remote.deliver(getServerSession(), "/getDocument", output, null);
  }

  public void getDocumentList(ServerSession remote, Message message)
  {
        Map<String, Object> input = message.getDataAsMap();

        Set docSet = sourceManager.getDocList();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> output = new HashMap<String, Object>();
        try {
          String json = mapper.writeValueAsString(docSet); 
          logger.debug(json);
          output.put("documentList", docSet);
        }
        catch(IOException e)
        {
          logger.error(e.toString());
          output.put("documentList", "fail");
        }

        remote.deliver(getServerSession(), "/getDocumentList", output, null);
  }



}
