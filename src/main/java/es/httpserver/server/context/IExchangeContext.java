package es.httpserver.server.context;

import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User: admin
 * Date: 19/10/2017
 * Time: 10:53
 */
public interface IExchangeContext {

    List<String> getRequestUriPath(String rootPath);

    HashMap<String, String> getRequestUriParameters();

    HashMap<String, String> getRequestBodyParameters() throws UnsupportedEncodingException;

    HashMap<String, String> getQueryParameters(String allQueryParameters);

    Headers getResponseHeaders();

    HashMap<String, String> getRequestHeaders();

    String getRequestMethod();

    InetSocketAddress getRemoteAddress();

    String getSessionCookie();

    String createSessionCookie();

    void sendResponse(int httpStatusCode, String responseBody) throws IOException;

}
