package es.httpserver.server.context;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import es.httpserver.common.Constants;
import es.httpserver.common.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by User: admin
 * Date: 19/10/2017
 * Time: 10:45
 */
public class ExchangeContext implements IExchangeContext {

    private static final Logger logger = LogManager.getLogger(ExchangeContext.class.getName());

    private final HttpExchange httpExchange;

    public ExchangeContext(HttpExchange httpExchange) {this.httpExchange = httpExchange;}


    @Override public List<String> getRequestUriPath(String rootPath) {

        String queryRequestPath = httpExchange.getRequestURI().getPath() != null ? httpExchange.getRequestURI().getPath() : "";

        List<String> getParams = Utils.separaCamposPorDelimitador(queryRequestPath, "/");

        return getParams.stream().filter(parametro -> !rootPath.contains(parametro)).collect(Collectors.toList());

    }

    @Override public HashMap<String, String> getRequestUriParameters() {

        String queryRequest = httpExchange.getRequestURI().getQuery() != null ? httpExchange.getRequestURI().getQuery() : "";

        return getQueryParameters(queryRequest);

    }

    @Override public HashMap<String, String> getRequestBodyParameters() throws UnsupportedEncodingException {

        InputStream requestBody = httpExchange.getRequestBody();
        String bodyString = new BufferedReader(new InputStreamReader(requestBody)).lines().collect(Collectors.joining("\n"));

        return getQueryParameters(bodyString);
    }


    @Override public HashMap<String, String> getQueryParameters(String allQueryParameters) {

        HashMap<String, String> listaParametrosValor = new HashMap<>();

        logger.debug("getQueryParameters - " + allQueryParameters);

        StringTokenizer listaTuplasClaveValor = new StringTokenizer(allQueryParameters, "&");
        while (listaTuplasClaveValor.hasMoreElements()) {
            String tuplaClaveValor = listaTuplasClaveValor.nextToken().trim();
            String[] arrayClaveValor = tuplaClaveValor.split("=");
            try {
                String parametro = URLDecoder.decode(arrayClaveValor[0], "UTF-8");
                String valor = arrayClaveValor[1] != null ? URLDecoder.decode(arrayClaveValor[1], "UTF-8") : "";
                logger.debug("Leido Parametro [" + parametro + " -> " + valor + "]");
                listaParametrosValor.put(parametro, valor);
            } catch (Exception e) {
                logger.debug("Exception: " + e.getMessage());
                e.printStackTrace();
            }

        }
        return listaParametrosValor;
    }

    @Override public Headers getResponseHeaders() {
        return httpExchange.getResponseHeaders();
    }

    @Override public HashMap<String, String> getRequestHeaders() {

        HashMap<String, String> listaParametrosValor = new HashMap<>();

        Headers requestHeaders = httpExchange.getRequestHeaders();

        Set<String> keyHeadersSet = requestHeaders.keySet();
        keyHeadersSet.forEach(headerKey -> {
            List headerKeyValues = requestHeaders.get(headerKey);
            logger.debug("HEADER: " + headerKey + " -> " + headerKeyValues.toString());
            listaParametrosValor.put(headerKey, headerKeyValues.toString());
        });

        return listaParametrosValor;
    }

    @Override public String getRequestMethod() {
        return httpExchange.getRequestMethod();
    }

    @Override public InetSocketAddress getRemoteAddress() {
        return httpExchange.getRemoteAddress();
    }

    /**
     * Genera un ID de session a partir de:
     * - HTTP Cookie Header o
     * - HOST de origen dela peticion
     *
     * @return sessionId
     */
    public String getSessionCookie() {

        String sessionId;
        if (httpExchange.getRequestHeaders().get(Constants.HEADER_COOKIE) != null) {
            sessionId = httpExchange.getRequestHeaders().get(Constants.HEADER_COOKIE).get(0);
            logger.debug("getSessionCookie - Readed Session [" + sessionId + "]");
        } else {
            // Si no viene una cookie -> la generamos
            sessionId = UUID.randomUUID().toString();
            httpExchange.getResponseHeaders().put(Constants.HEADER_SET_COOKIE, new ArrayList<String>() {
                {
                    add(sessionId);
                }
            });
            logger.debug("getSessionCookie - Created new Session [" + sessionId + "]");
        }

        return sessionId;
    }

    public void sendResponse(int httpStatusCode, String responseBody) throws IOException {
        httpExchange.sendResponseHeaders(httpStatusCode, responseBody.getBytes().length);
        httpExchange.getResponseBody().write(responseBody.getBytes());
        httpExchange.close();
    }

}
