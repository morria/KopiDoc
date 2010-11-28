package io.adroit.kopiDoc.service;

import io.adroit.kopiDoc.source.SourceManager;
import io.adroit.kopiDoc.source.JsonRootDocSerializer;
import com.sun.javadoc.RootDoc;
import java.io.IOException;
import java.util.Collection;
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

    addService("/service/getFile", "getFile");
    addService("/service/getFileList", "getFileList");

    addService("/service/getClass", "getClass");
    addService("/service/getClassList", "getClassList");
  }

  /**
    *
    */
  public void addSources(ServerSession remote, Message message)
  {
    Map<String, Object> input = message.getDataAsMap();
    String sourcePath = (String)input.get("sourcePath");
    String classPath = (String)input.get("classPath");

    boolean success =
      sourceManager.addSources(sourcePath,classPath);

    Map<String, Object> output = new HashMap<String, Object>();
    output.put("success", success? "true":"false");
    remote.deliver(getServerSession(), "/addSources", output, null);
  }


  /**
    *
    */
  public void getFile(ServerSession remote, Message message)
  {
    Map<String, Object> input = message.getDataAsMap();
    String absolutePath = (String)input.get("absolutePath");

    String jsonDocument = 
      sourceManager.getJSONDocumentByFileName(absolutePath);

    Map<String, Object> output = new HashMap<String, Object>();
    output.put("document", jsonDocument);

    remote.deliver(getServerSession(), "/getFile", output, null);
  }

  /**
    *
    */
  public void getFileList(ServerSession remote, Message message)
  {
    Collection<String> fileList = sourceManager.getFileList();

    Map<String, Object> output = new HashMap<String, Object>();
    output.put("fileList", fileList);

    remote.deliver(getServerSession(), "/getFileList", output, null);
  }

  /**
    *
    */
  public void getClassList(ServerSession remote, Message message)
  {
    Collection classList = sourceManager.getClassList();

    Map<String, Object> output = new HashMap<String, Object>();
    output.put("classList", classList);

    remote.deliver(getServerSession(), "/getClassList", output, null);
  }

  /**
    *
    */
  public void getClass(ServerSession remote, Message message)
  {
    Map<String, Object> input = message.getDataAsMap();
    String qualifiedClassName = (String)input.get("qualifiedClassName");

    String jsonDocument = 
      sourceManager.getJSONDocumentByClassName(qualifiedClassName);

    Map<String, Object> output = new HashMap<String, Object>();
    output.put("document", jsonDocument);

    remote.deliver(getServerSession(), "/getClass", output, null);
  }



}
