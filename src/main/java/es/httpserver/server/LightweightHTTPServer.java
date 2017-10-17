package es.httpserver.server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import es.httpserver.authentication.BasicAuthenticationChecker;
import es.httpserver.common.Constants;
import es.httpserver.server.handlers.LoginHandler;
import es.httpserver.server.handlers.LogoutHandler;
import es.httpserver.server.handlers.UsersHandler;
import es.httpserver.server.handlers.WebHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 10:10
 */
public class LightweightHTTPServer {


    private static final int DEFAULT_PORT = 8080;
    // Server Listen port
    private static int defaultPort = DEFAULT_PORT;
    // The backlog defines the limit for the queue of incoming connections.
    // -> If this value is less than or equal to zero, then a system default value is used.
    private static int defaultBacklog = 0;

    public static void main(String[] args) throws IOException {

        System.out.println("Starting LightweightHTTPServer... ");

        if (args.length > 0) {
            try {
                defaultPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException exception) {
                System.out.println("Incorrect port number, using port " + DEFAULT_PORT);
            }
        }

        InetSocketAddress inetSocketAddress = new InetSocketAddress(defaultPort);
        HttpServer lightweightHTTPServer = HttpServer.create(inetSocketAddress, defaultBacklog);

        // All requests received by the server for the path will be handled by calling the given handler object
        lightweightHTTPServer.createContext(Constants.ROOT_CONTEXTPATH, new WebHandler());
        lightweightHTTPServer.createContext(Constants.WEB_CONTEXTPATH, new WebHandler());

        lightweightHTTPServer.createContext(Constants.LOGIN_CONTEXTPATH, new LoginHandler());
        lightweightHTTPServer.createContext(Constants.LOGOUT_CONTEXTPATH, new LogoutHandler());
        // REST User Service
        HttpContext userWSRESTContext = lightweightHTTPServer.createContext(Constants.USERS_CONTEXTPATH, new UsersHandler());
        userWSRESTContext.setAuthenticator(new BasicAuthenticationChecker("users"));

        // All HTTP requests are handled in tasks given to the executor -> object that executes submitted Runnable tasks
        // newCachedThreadPool() -> Creates a thread pool that creates new threads as needed, but will reuse previously constructed threads when they are available.
        lightweightHTTPServer.setExecutor(Executors.newCachedThreadPool());
        // Server ups!
        lightweightHTTPServer.start();

        System.out.println("es.httpserver.server.LightweightHTTPServer is up! and listening on port " + defaultPort);

        System.out.println("Press Ctrl-C to terminate.");
    }

}
