package es.httpserver.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.httpserver.common.Constants;
import es.httpserver.controllers.SessionController;
import es.httpserver.controllers.UsersDataController;
import es.httpserver.server.context.ExchangeContext;
import es.httpserver.server.context.IExchangeContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.stream.Collectors;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 12:22
 */
public class HTTPCommonHandler implements HttpHandler {

    private static final Logger logger = LogManager.getLogger(HTTPCommonHandler.class.getName());

    protected IExchangeContext exchangeContext;

    protected SessionController sessionController;
    protected UsersDataController usersDataController;

    @Override public void handle(HttpExchange requestHttpExchange) throws IOException {

        exchangeContext = new ExchangeContext(requestHttpExchange);
        sessionController = SessionController.getInstance();
        usersDataController = UsersDataController.getInstance();

        String requestMethod = exchangeContext.getRequestMethod().toLowerCase();
        logger.debug("Procesando peticion del cliente: " + exchangeContext.getRemoteAddress().getHostName());

        try {
            Method method = this.getClass().getDeclaredMethod(requestMethod);
            method.invoke(this);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            sendNotFoundResponse();
        } catch (Exception ex) {
            ex.printStackTrace();
            sendServerError();
        }

    }


    protected void sendCreatedResponse(String responseBody) throws IOException {
        exchangeContext.sendResponse(HttpURLConnection.HTTP_CREATED, responseBody);
    }

    protected void sendSuccessfulResponse(String responseBody) throws IOException {
        exchangeContext.sendResponse(HttpURLConnection.HTTP_OK, responseBody);
    }

    protected void sendForbiddenResponse(String responseBody) throws IOException {
        exchangeContext.sendResponse(HttpURLConnection.HTTP_FORBIDDEN, responseBody);
    }

    protected void sendBadRequestResponse(String msg) throws IOException {
        exchangeContext.sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, "400 Bad Request :" + msg);
    }

    protected void sendNotFoundResponse() throws IOException {
        exchangeContext.sendResponse(HttpURLConnection.HTTP_NOT_FOUND, "404 Not Found");
    }

    protected void sendServerError() throws IOException {
        exchangeContext.sendResponse(HttpURLConnection.HTTP_UNAVAILABLE, "503 Server Error");
    }

    protected void sendRedirectTemporarly(String responseBody) throws IOException {
        exchangeContext.sendResponse(HttpURLConnection.HTTP_MOVED_TEMP, responseBody);
    }

    protected SessionController getSessionController() {
        return sessionController;
    }

    protected UsersDataController getUsersDataController() {
        return usersDataController;
    }

    /**
     * Este metodo sustituye en los ficheros HTML las variables:
     * #$USERNAME$# -> userName
     * #$ERROR$# -> errorMSG
     *
     * @param pathToHTMLTemplate pagina HTML a procesar
     * @param userName           nombre del usuario
     * @param errorMSG           Mensage en caso de error
     * @return
     * @throws IOException
     */
    protected String generateHTMLPage(String pathToHTMLTemplate, String userName, String errorMSG) throws IOException {

        logger.debug("generateHTMLPage - " + pathToHTMLTemplate + " [" + userName + "," + errorMSG + "]");

        InputStream template = this.getClass().getResourceAsStream(pathToHTMLTemplate);
        StringBuilder stringBuilderTemplate = new StringBuilder(new BufferedReader(new InputStreamReader(template)).lines().collect(Collectors.joining("\n")));
        int index;
        while ((index = stringBuilderTemplate.lastIndexOf(Constants.WEB_USERNAME_PATTERN)) != -1) {
            stringBuilderTemplate.replace(index, index + Constants.WEB_USERNAME_PATTERN.length(), userName);
        }
        while ((index = stringBuilderTemplate.lastIndexOf(Constants.WEB_ERROR_PATTERN)) != -1) {
            stringBuilderTemplate.replace(index, index + Constants.WEB_ERROR_PATTERN.length(), errorMSG);
        }
        return stringBuilderTemplate.toString();
    }





}
