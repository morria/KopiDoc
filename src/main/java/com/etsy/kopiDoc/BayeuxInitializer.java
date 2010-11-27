package com.etsy.kopiDoc;

import java.io.IOException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.cometd.bayeux.server.BayeuxServer;

public class BayeuxInitializer extends GenericServlet
{
    public void init() throws ServletException
    {
        BayeuxServer bayeux = 
          (BayeuxServer)getServletContext().getAttribute(BayeuxServer.ATTRIBUTE);

        new HelloService(bayeux);
        new DocumentService(bayeux);
    }

    public void service(ServletRequest request, ServletResponse response) 
      throws ServletException, IOException
    {
        throw new ServletException();
    }
}
