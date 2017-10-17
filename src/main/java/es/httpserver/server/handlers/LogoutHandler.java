package es.httpserver.server.handlers;

import es.httpserver.common.Constants;

import java.io.IOException;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 12:15
 */
public class LogoutHandler extends HTTPCommonHandler {

    public void post() throws IOException {

        System.out.println("LogoutHandler - post");

        String sessionId = getSesionHeaderHash();

        System.out.println("Solicitado el Logout de la session " + sessionId);
        String usuarioDeSession = getSessionController().removeSessionInfo(sessionId);

        sendSuccessfulResponse(generateHTMLPage(Constants.LOGIN_PAGE_PATH, "", "Logout OK para " + usuarioDeSession));
    }

}
