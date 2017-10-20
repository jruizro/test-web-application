package es.httpserver.server;

import com.sun.net.httpserver.HttpServer;
import es.httpserver.authentication.BasicAuthenticationChecker;
import es.httpserver.controllers.ContextController;
import es.httpserver.model.ContextPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 10:10
 */
public class LightweightHTTPServer {

    private static final Logger logger = LogManager.getLogger(LightweightHTTPServer.class.getName());

    private static final int DEFAULT_PORT = 8080;
    // Server Listen port
    private static int defaultPort = DEFAULT_PORT;
    // The backlog defines the limit for the queue of incoming connections.
    // -> If this value is less than or equal to zero, then a system default value is used.
    private static int defaultBacklog = 0;


    public static void main(String[] args) throws IOException {

        logger.info("Starting LightweightHTTPServer... ");

        if (args.length > 0) {
            try {
                defaultPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException exception) {
                logger.debug("Incorrect port number, using default port " + DEFAULT_PORT);
            }
        }

        InetSocketAddress inetSocketAddress = new InetSocketAddress(defaultPort);
        HttpServer lightweightHTTPServer = HttpServer.create(inetSocketAddress, defaultBacklog);

        // All requests received by the server for the path will be handled by calling the given handler object
        ContextController contextController = new ContextController(lightweightHTTPServer);
        contextController.buildDefaultContexts();

        // REST User Service uses Basic Authorization
        contextController.addAuthenticationToContext(ContextPath.USERS_WS, new BasicAuthenticationChecker("users"));

        // All HTTP requests are handled in tasks given to the executor -> object that executes submitted Runnable tasks
        // newCachedThreadPool() -> Creates a thread pool that creates new threads as needed, but will reuse previously constructed threads when they are available.
        lightweightHTTPServer.setExecutor(Executors.newCachedThreadPool());
        // Server ups!
        lightweightHTTPServer.start();

        logger.debug("LightweightHTTPServer is up! and listening on port " + defaultPort);

        logger.debug("Press Ctrl-C to terminate.");
    }

}
