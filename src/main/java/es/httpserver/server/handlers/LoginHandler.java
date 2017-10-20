package es.httpserver.server.handlers;

import es.httpserver.authentication.BasicAuthenticationChecker;
import es.httpserver.common.Constants;
import es.httpserver.controllers.NavigationController;
import es.httpserver.model.IWebSession;
import es.httpserver.model.WebSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 12:15
 */
public class LoginHandler extends HTTPCommonHandler {

    private static final Logger logger = LogManager.getLogger(LoginHandler.class.getName());

    public void post() throws IOException {

        logger.debug("LoginHandler - post");

        NavigationController navigationController = new NavigationController();
        HashMap<String, String> bodyParameters = exchangeContext.getRequestBodyParameters();

        boolean isValidLogin = false;

        String username = bodyParameters.get(Constants.REQ_PARAM_USERNAME);
        String password = bodyParameters.get(Constants.REQ_PARAM_PASSWORD);

        if (username != null && password != null) {
            logger.debug("Checking credentials for [" + username + " -> " + password + "]");
            BasicAuthenticationChecker basicAuthenticationChecker = new BasicAuthenticationChecker("login");
            isValidLogin = basicAuthenticationChecker.checkCredentials(username, password);
        }

        if (isValidLogin) {

            String sessionId = exchangeContext.getSessionCookie();

            if (getSessionController().existSessionId(sessionId)) {

                // Asignamos la session existente al usuario que se ha logado
                IWebSession sessionUpdated = getSessionController().updateUserSession(sessionId, getUsersDataController().getUser(username));

                if (sessionUpdated.getReferer() != null) {

                    String paginaSolicitada = getSessionController().getSession(sessionId).getReferer();
                    logger.debug("User " + username + " pending viewing -> " + paginaSolicitada);
                    logger.debug("Login OK for user " + username + " -> Session info updated");

                    // Verificamos si tras el nuevo login tiene acceso a la pagina anteriormente solicitada
                    if (navigationController.hasAccessToPage(sessionUpdated, paginaSolicitada)) {
                        // Si ya hay una session anonima -> Update Session & acceso a pagina Solicitada
                        sendSuccessfulResponse(generateHTMLPage(navigationController.getPagePath(paginaSolicitada), username, ""));
                    } else {
                        // Si de nuevo NO tiene acceso a la pagina solicitada -> Home
                        sendSuccessfulResponse(generateHTMLPage(Constants.HOME_PAGE_PATH, username, "ERROR: User does not have the necessary role to " + paginaSolicitada));
                    }
                } else {
                    // Si no tiene pagina pendiente de ver -> HOME
                    sendSuccessfulResponse(generateHTMLPage(Constants.HOME_PAGE_PATH, username, ""));
                }

            } else {
                // New session y acceso al HOME
                logger.debug("Login OK for user " + username + " -> Created a new session");
                getSessionController().addSession(new WebSession(sessionId, getUsersDataController().getUser(username)));
                sendSuccessfulResponse(generateHTMLPage(Constants.HOME_PAGE_PATH, username, ""));
            }

        } else {
            logger.debug("Login KO for user " + username);
            sendSuccessfulResponse(generateHTMLPage(Constants.LOGIN_PAGE_PATH, username, "ERROR: Invalid Credentials"));
        }
    }


}
