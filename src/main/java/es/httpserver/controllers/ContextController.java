package es.httpserver.controllers;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import es.httpserver.model.ContextPath;
import es.httpserver.server.handlers.FactoryHandler;

import java.util.HashMap;

/**
 * Created by User: admin
 * Date: 20/10/2017
 * Time: 10:28
 */
public class ContextController {

    private HttpServer server;
    private HashMap<ContextPath, HttpContext> serverContextList = new HashMap<>();

    public ContextController(HttpServer server) {
        this.server = server;
    }

    public void buildDefaultContexts() {
        for (ContextPath context : ContextPath.values()) {
            HttpContext createdContext = server.createContext(context.getStringPath(), FactoryHandler.getHandler(context));
            serverContextList.put(context,createdContext);
        }
    }

    public HttpContext addContext(ContextPath context){
        HttpContext createdContext = server.createContext(context.getStringPath(), FactoryHandler.getHandler(context));
        serverContextList.put(context,createdContext);
        return createdContext;
    }

    public HttpContext getContext(ContextPath context){
        return serverContextList.get(context);
    }

    public HttpContext removeContext(ContextPath context){
        server.removeContext(context.getStringPath());
        return serverContextList.remove(context);
    }

    public void addAuthenticationToContext(ContextPath context, BasicAuthenticator authenticator){
        serverContextList.get(context).setAuthenticator(authenticator);
    }

}
