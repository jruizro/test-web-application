package es.httpserver.server.handlers;

import es.httpserver.common.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 10:15
 */
public class RootHandler extends HTTPCommonHandler {

    private static final Logger logger = LogManager.getLogger(RootHandler.class.getName());

    public void get() throws IOException {

        logger.debug("RootHandler - get");

        String sessionId = exchangeContext.getSessionCookie();
        if (!getSessionController().existSessionId(sessionId)) {
            sessionId = exchangeContext.createSessionCookie();
            getSessionController().addNoLoginSession(sessionId, null);
        }
        sendForbiddenResponse(generateHTMLPage(Constants.LOGIN_PAGE_PATH, "", ""));

    }

}
