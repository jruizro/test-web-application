package es.httpserver.server.handlers;

import es.httpserver.common.Constants;
import es.httpserver.controllers.NavigationController;
import es.httpserver.model.IWebSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 10:15
 */
public class WebHandler extends HTTPCommonHandler {

    private static final Logger logger = LogManager.getLogger(WebHandler.class.getName());

    public void get() throws IOException {

        logger.debug("WebHandler - get");

        try {

            NavigationController navigationController = new NavigationController();
            HashMap<String, String> uriParameters = exchangeContext.getRequestUriParameters();

            String paginaSolicitada = uriParameters.get(Constants.PAGE_PARAM);
            String sessionId = exchangeContext.getSessionCookie();

            if (paginaSolicitada == null) {
                if (getSessionController().existSessionId(sessionId) && getSessionController().getSession(sessionId).getUser() != null) {
                    // User is Logged -> default Page is Home
                    paginaSolicitada = Constants.HOME_PAGE_PATH;
                } else {
                    // User is not Logged -> default Page is Login
                    paginaSolicitada = Constants.LOGIN_PAGE_PATH;
                }
            }

            if (paginaSolicitada.equalsIgnoreCase(Constants.LOGIN_PAGE_PATH)) {
                // Para acceder a la pagina de Login no es necesario tener session
                sendSuccessfulResponse(generateHTMLPage(paginaSolicitada, "", ""));

            } else if (getSessionController().existSessionId(sessionId)) {
                if (!getSessionController().isExpired(sessionId)) {
                    IWebSession session = getSessionController().getSession(sessionId);
                    if (navigationController.hasAccessToPage(session, paginaSolicitada)) {
                        sendSuccessfulResponse(generateHTMLPage(navigationController.getPagePath(paginaSolicitada), session.getUser().getUsername(), ""));
                    } else {
                        // Usuario sin permisos para acceder -> LOGIN again
                        // Para esa session marcamos como pagina futura la solicitada
                        IWebSession noLoginsession = getSessionController().addNoLoginSession(sessionId, paginaSolicitada);
                        sendForbiddenResponse(generateHTMLPage(Constants.LOGIN_PAGE_PATH, noLoginsession.getUser().getUsername(), "No ROLE to access page " + paginaSolicitada));
                    }
                } else {
                    // Ha expirado la sesion
                    getSessionController().removeSession(sessionId);
                    sendForbiddenResponse(generateHTMLPage(Constants.LOGIN_PAGE_PATH, "", "Your Session has Expired!"));
                }
            } else {
                // El usuario no esta logado
                getSessionController().addNoLoginSession(sessionId, paginaSolicitada);
                sendForbiddenResponse(generateHTMLPage(Constants.LOGIN_PAGE_PATH, "", "Need to Log in to access " + paginaSolicitada));
            }

        } catch (Exception e) {
            sendServerError(generateHTMLPage(Constants.ERROR_PAGE_PATH, "", "Internarl Error: " + e.getMessage()));
        }
    }

}
