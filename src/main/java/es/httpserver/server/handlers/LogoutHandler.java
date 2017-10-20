package es.httpserver.server.handlers;

import es.httpserver.common.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 12:15
 */
public class LogoutHandler extends HTTPCommonHandler {

    private static final Logger logger = LogManager.getLogger(LogoutHandler.class.getName());

    public void post() throws IOException {

        logger.debug("LogoutHandler - post");

        String sessionId = exchangeContext.getSessionCookie();

        logger.debug("Requested Session Logout for " + sessionId);
        String usuarioDeSession = getSessionController().removeSession(sessionId);

        sendSuccessfulResponse(generateHTMLPage(Constants.LOGIN_PAGE_PATH, "", "Logout OK for " + usuarioDeSession));
    }

}
