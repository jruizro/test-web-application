package es.httpserver.server.handlers;

import com.sun.net.httpserver.HttpHandler;
import es.httpserver.model.ContextPath;

/**
 * Created by User: admin
 * Date: 20/10/2017
 * Time: 9:17
 */
public class FactoryHandler {

    public static HttpHandler getHandler(ContextPath contextPath) {

        switch (contextPath) {
            case ROOT:
                return new RootHandler();
            case LOGIN:
                return new LoginHandler();
            case LOGOUT:
                return new LogoutHandler();
            case WEB:
                return new WebHandler();
            case USERS_WS:
                return new UsersHandler();
            default:
                return null;
        }

    }

}
