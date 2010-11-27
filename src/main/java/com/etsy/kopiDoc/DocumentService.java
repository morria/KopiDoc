package com.etsy.kopiDoc;

import java.util.Map;
import java.util.HashMap;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;

public class DocumentService extends AbstractService
{
    public DocumentService(BayeuxServer bayeux)
    {
        super(bayeux, "document");
        addService("/service/document", "processDocument");
    }

    public void processDocument(ServerSession remote, Message message)
    {
        Map<String, Object> input = message.getDataAsMap();
        String className = (String)input.get("className");

        Map<String, Object> output = new HashMap<String, Object>();
        output.put("document", className);
        remote.deliver(getServerSession(), "/document", output, null);
    }
}
