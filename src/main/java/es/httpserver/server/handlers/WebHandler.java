package es.httpserver.server.handlers;

import es.httpserver.common.Constants;
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

        HashMap<String, String> uriParameters = getRequestUriParameters();

        String paginaSolicitada = uriParameters.get(Constants.PAGE_PARAM) != null ? uriParameters.get(Constants.PAGE_PARAM) : Constants.LOGIN_PAGE_PATH;
        String sessionId = getSesionHeaderHash();

        if (paginaSolicitada.equalsIgnoreCase(Constants.LOGIN_PAGE_PATH)) {
            // Para acceder a la pagina de Login no es necesario tener session
            sendSuccessfulResponse(generateHTMLPage(paginaSolicitada, "", ""));

        } else if (getSessionController().existSessionId(sessionId)) {
            if (!getSessionController().isExpired(sessionId)) {
                if (getSessionController().hasAccessToPage(sessionId, paginaSolicitada)) {
                    sendSuccessfulResponse(generateHTMLPage(getPagePath(paginaSolicitada), getSessionController().getSessionUserName(sessionId), ""));
                } else {
                    // Usuario sin permisos para acceder -> LOGIN again
                    // Para esa session marcamos como pagina futura la solicitada
                    getSessionController().addNoLoginSessionInfo(sessionId, paginaSolicitada);
                    sendForbiddenResponse(generateHTMLPage(Constants.LOGIN_PAGE_PATH, getSessionController().getSessionUserName(sessionId), "No ROLE to access page " + paginaSolicitada));
                }
            } else {
                // Ha expirado la sesion
                getSessionController().removeSessionInfo(sessionId);
                sendForbiddenResponse(generateHTMLPage(Constants.LOGIN_PAGE_PATH, "", "Your Session has Expired!"));
            }
        } else {
            // El usuario no esta logado
            getSessionController().addNoLoginSessionInfo(sessionId, paginaSolicitada);
            sendForbiddenResponse(generateHTMLPage(Constants.LOGIN_PAGE_PATH, "", "Need to Log in to access " + paginaSolicitada));
        }

    }

}
