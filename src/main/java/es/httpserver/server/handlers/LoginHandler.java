package es.httpserver.server.handlers;

import es.httpserver.authentication.BasicAuthenticationChecker;
import es.httpserver.common.Constants;
import es.httpserver.model.WebSession;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 12:15
 */
public class LoginHandler extends HTTPCommonHandler {

    public void post() throws IOException {

        System.out.println("LoginHandler - post");

        HashMap<String, String> bodyParameters = getRequestBodyParameters();

        boolean isValidLogin = false;

        String username = bodyParameters.get("username");
        String password = bodyParameters.get("password");

        if (username != null && password != null) {
            System.out.println("Validando credenciales para [" + username + " -> " + password + "]");
            BasicAuthenticationChecker basicAuthenticationChecker = new BasicAuthenticationChecker("login");
            isValidLogin = basicAuthenticationChecker.checkCredentials(username, password);
        }

        if (isValidLogin) {

            String sessionId = getSesionHeaderHash();

            if (getSessionController().existSessionId(sessionId) && getSessionController().getSessionInfo(sessionId).getNextPage() != null) {

                String paginaSolicitada = getSessionController().getSessionInfo(sessionId).getNextPage();
                System.out.println("Usuario " + username + " pendiente de visualizar -> " + paginaSolicitada);
                System.out.println("Login OK para el usuario " + username + " -> Se actualiza su session");
                getSessionController().addSessionInfo(new WebSession(sessionId, getUsersDataController().getUser(username)));
                // Si ya hay una session anonima -> Update Session & acceso a pagina Solicitada
                sendSuccessfulResponse(generateHTMLPage(getPagePath(paginaSolicitada), username, ""));
            } else {
                // New session y acceso al HOME
                System.out.println("Login OK para el usuario " + username + " -> Se crea su session");
                getSessionController().addSessionInfo(new WebSession(sessionId, getUsersDataController().getUser(username)));
                sendSuccessfulResponse(generateHTMLPage(Constants.HOME_PAGE_PATH, username, ""));
            }

        } else {
            System.out.println("Login KO para el usuario " + username);
            sendSuccessfulResponse(generateHTMLPage(Constants.LOGIN_PAGE_PATH, username, "ERROR: Invalid Credentials"));
        }
    }


}
