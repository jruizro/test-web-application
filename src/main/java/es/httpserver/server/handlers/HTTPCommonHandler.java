package es.httpserver.server.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.httpserver.common.Constants;
import es.httpserver.controllers.SessionController;
import es.httpserver.controllers.UsersDataController;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 12:22
 */
public class HTTPCommonHandler implements HttpHandler {

    private HttpExchange httpExchange;
    private SessionController sessionController;
    private UsersDataController usersDataController;

    @Override public void handle(HttpExchange requestHttpExchange) throws IOException {
        httpExchange = requestHttpExchange;
        sessionController = SessionController.getInstance();
        usersDataController = UsersDataController.getInstance();

        String requestMethod = httpExchange.getRequestMethod().toLowerCase();
        System.out.println("Procesando peticion del cliente: " + httpExchange.getRemoteAddress().getHostName());

        getRequestHeaders();

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

    protected List<String> getRequestUriPath(String rootPath) {

        List<String> getParams = new Vector<>();

        String queryRequestPath = httpExchange.getRequestURI().getPath() != null ? httpExchange.getRequestURI().getPath() : "";

        StringTokenizer listaParamentrosGet = new StringTokenizer(queryRequestPath, "/");
        while (listaParamentrosGet.hasMoreElements()) {
            String parametroGet = listaParamentrosGet.nextToken().trim();
            System.out.println("getQueryParameter - " + parametroGet);
            if (!rootPath.contains(parametroGet)) {
                getParams.add(parametroGet);
            }
        }
        return getParams;

    }


    protected HashMap<String, String> getRequestUriParameters() {

        String queryRequest = httpExchange.getRequestURI().getQuery() != null ? httpExchange.getRequestURI().getQuery() : "";

        return getQueryParameters(queryRequest);

    }

    protected HashMap<String, String> getRequestBodyParameters() throws UnsupportedEncodingException {

        InputStream requestBody = getHttpExchange().getRequestBody();
        String bodyString = new BufferedReader(new InputStreamReader(requestBody)).lines().collect(Collectors.joining("\n"));

        return getQueryParameters(bodyString);
    }


    protected HashMap<String, String> getQueryParameters(String allQueryParameters) {

        HashMap<String, String> listaParametrosValor = new HashMap<>();

        System.out.println("getQueryParameters - " + allQueryParameters);

        StringTokenizer listaTuplasClaveValor = new StringTokenizer(allQueryParameters, "&");
        while (listaTuplasClaveValor.hasMoreElements()) {
            String tuplaClaveValor = listaTuplasClaveValor.nextToken().trim();
            String[] arrayClaveValor = tuplaClaveValor.split("=");
            try {
                String parametro = URLDecoder.decode(arrayClaveValor[0], "UTF-8");
                String valor = arrayClaveValor[1] != null ? URLDecoder.decode(arrayClaveValor[1], "UTF-8") : "";
                System.out.println("Leido Parametro [" + parametro + " -> " + valor + "]");
                listaParametrosValor.put(parametro, valor);
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
                e.printStackTrace();
            }

        }
        return listaParametrosValor;
    }

    protected Headers getResponseHeaders() {
        return httpExchange.getResponseHeaders();
    }

    protected HashMap<String, String> getRequestHeaders() {

        HashMap<String, String> listaParametrosValor = new HashMap<>();

        Headers requestHeaders = httpExchange.getRequestHeaders();
        Set<String> keyHeadersSet = requestHeaders.keySet();
        Iterator<String> iteradorHeaders = keyHeadersSet.iterator();
        while (iteradorHeaders.hasNext()) {
            String headerKey = iteradorHeaders.next();
            List headerKeyValues = requestHeaders.get(headerKey);
            System.out.println("HEADER: " + headerKey + " -> " + headerKeyValues.toString());
            listaParametrosValor.put(headerKey, headerKeyValues.toString());

        }
        return listaParametrosValor;
    }

    private void sendResponse(int httpStatusCode, String responseBody) throws IOException {
        httpExchange.sendResponseHeaders(httpStatusCode, responseBody.getBytes().length);
        httpExchange.getResponseBody().write(responseBody.getBytes());
        httpExchange.close();
    }

    protected void sendCreatedResponse(String responseBody) throws IOException {
        sendResponse(HttpURLConnection.HTTP_CREATED, responseBody);
    }

    protected void sendSuccessfulResponse(String responseBody) throws IOException {
        sendResponse(HttpURLConnection.HTTP_OK, responseBody);
    }

    protected void sendForbiddenResponse(String responseBody) throws IOException {
        sendResponse(HttpURLConnection.HTTP_FORBIDDEN, responseBody);
    }

    protected void sendBadRequestResponse(String msg) throws IOException {
        sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, "400 Bad Request :" + msg);
    }

    protected void sendNotFoundResponse() throws IOException {
        sendResponse(HttpURLConnection.HTTP_NOT_FOUND, "404 Not Found");
    }

    protected void sendServerError() throws IOException {
        sendResponse(HttpURLConnection.HTTP_UNAVAILABLE, "503 Server Error");
    }

    protected void sendRedirectTemporarly(String responseBody) throws IOException {
        sendResponse(HttpURLConnection.HTTP_MOVED_TEMP, responseBody);
    }

    protected HttpExchange getHttpExchange() {
        return httpExchange;
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
     * @param pathToHTMLTemplate
     * @param userName
     * @param errorMSG
     */
    protected String generateHTMLPage(String pathToHTMLTemplate, String userName, String errorMSG) throws IOException {

        System.out.println("generateHTMLPage - " + pathToHTMLTemplate + " [" + userName + "," + errorMSG + "]");

        InputStream template = this.getClass().getResourceAsStream(pathToHTMLTemplate);
        StringBuilder stringBuilderTemplate = new StringBuilder(new BufferedReader(new InputStreamReader(template)).lines().collect(Collectors.joining("\n")));
        int index = -1;
        while ((index = stringBuilderTemplate.lastIndexOf(Constants.WEB_USERNAME_PATTERN)) != -1) {
            stringBuilderTemplate.replace(index, index + Constants.WEB_USERNAME_PATTERN.length(), userName);
        }
        while ((index = stringBuilderTemplate.lastIndexOf(Constants.WEB_ERROR_PATTERN)) != -1) {
            stringBuilderTemplate.replace(index, index + Constants.WEB_ERROR_PATTERN.length(), errorMSG);
        }
        return stringBuilderTemplate.toString();
    }

    /**
     * Genera un ID de session a partir de:
     * - HTTP Cookie Header o
     * - HOST de origen dela peticion
     * @return
     */
    protected String getSesionHeaderHash() {

        String sessionId;
        if (httpExchange.getRequestHeaders().get(Constants.HEADER_COOKIE) != null) {
            String cookieHeader = httpExchange.getRequestHeaders().get(Constants.HEADER_COOKIE).get(0);
            sessionId = String.valueOf(cookieHeader.hashCode());
            System.out.println("getSesionHeaderHash [" + cookieHeader + " -> " + sessionId + "]");
        } else {
            String hostName = httpExchange.getRemoteAddress().getHostName();
            sessionId = String.valueOf(hostName.hashCode());
            System.out.println("getSesionHeaderHash [" + hostName + " -> " + sessionId + "]");
        }

        return sessionId;
    }


    protected String getPagePath(String paginaSolicitada) {

        switch (paginaSolicitada) {
            case Constants.PAGE_1_PARAMETER:
                return Constants.PAGE_1_PATH;
            case Constants.PAGE_2_PARAMETER:
                return Constants.PAGE_2_PATH;
            case Constants.PAGE_3_PARAMETER:
                return Constants.PAGE_3_PATH;
            default:
                return Constants.HOME_PAGE_PATH;
        }
    }

    protected List<String> separaCamposPorComas(String listaConComas) {
        List<String> listaSeparadaSinComas = new Vector<String>();
        StringTokenizer serviceTokenizer = new StringTokenizer(listaConComas, ",");
        while (serviceTokenizer.hasMoreElements()) {
            listaSeparadaSinComas.add(serviceTokenizer.nextToken().trim());
        }
        return listaSeparadaSinComas;
    }


}
