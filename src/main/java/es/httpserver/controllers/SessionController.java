package es.httpserver.controllers;

import es.httpserver.common.Constants;
import es.httpserver.model.WebSession;
import es.httpserver.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 12:40
 */
public class SessionController {

    private static final Logger logger = LogManager.getLogger(SessionController.class.getName());

    private static SessionController instance = null;

    private Map<String, WebSession> sessionsList = null;

    public static SessionController getInstance() {
        if (instance == null) {
            instance = new SessionController();
        }
        return instance;
    }

    private SessionController() {
        sessionsList = new HashMap<>();
        logger.debug("Session controller started with " + sessionsList.size() + " controllers");
    }

    public WebSession getSessionInfo(String sessionId) {
        return sessionsList.get(sessionId);
    }

    public void addSessionInfo(WebSession infoDeSesion) {
        sessionsList.put(infoDeSesion.getId(), infoDeSesion);
        logger.debug("Almacenada la sesion '" + infoDeSesion.getId() + "' del usuario '" + infoDeSesion.getUser().getUsername() + "'");
    }

    public void addNoLoginSessionInfo(String sessionId, String paginaSolicitada) {
        WebSession infoDeSesion = new WebSession(sessionId, new User());
        infoDeSesion.setNextPage(paginaSolicitada);
        sessionsList.put(sessionId, infoDeSesion);
        logger.debug("Almacenada la sesion '" + infoDeSesion.getId() + "' para un usuario NO LOGADO -> " + paginaSolicitada);
    }

    public String removeSessionInfo(String sessionId) {

        String ususarioDeLaSession = "";
        if (sessionsList.containsKey(sessionId)) {
            ususarioDeLaSession = sessionsList.get(sessionId).getUser().getUsername();
            logger.debug("Eliminando la sesion '" + sessionId + "' del usuario '" + ususarioDeLaSession + "'");
            sessionsList.remove(sessionId);
        }
        return ususarioDeLaSession;
    }

    public boolean hasAccessToPage(String sessionId, String paginaDestino) {

        boolean tienePermiso;
        WebSession userSession = sessionsList.get(sessionId);

        logger.debug("Session '" + sessionId + "' solicita permiso para '" + paginaDestino + "'");

        switch (paginaDestino) {
            case Constants.PAGE_1_PARAMETER:
                tienePermiso = userSession.getUser().getRoles().contains(Constants.ROLE_4_PAGE_1) || userSession.getUser().getRoles().contains(Constants.ROLE_ADMIN);
                break;
            case Constants.PAGE_2_PARAMETER:
                tienePermiso = userSession.getUser().getRoles().contains(Constants.ROLE_4_PAGE_2) || userSession.getUser().getRoles().contains(Constants.ROLE_ADMIN);
                break;
            case Constants.PAGE_3_PARAMETER:
                tienePermiso = userSession.getUser().getRoles().contains(Constants.ROLE_4_PAGE_3) || userSession.getUser().getRoles().contains(Constants.ROLE_ADMIN);
                break;
            case Constants.HOME_PAGE_PATH:
                tienePermiso = true;
                break;
            case Constants.LOGIN_PAGE_PATH:
                tienePermiso = true;
                break;
            default:
                throw new IllegalArgumentException("Invalid navigation page! " + paginaDestino);
        }

        return tienePermiso;
    }

    public String getSessionUserName(String sessionId) {
        return sessionsList.get(sessionId).getUser().getUsername();
    }

    public boolean isExpired(String sessionId) {
        return sessionsList.get(sessionId).isExpired();
    }

    public boolean existSessionId(String sessionId) {
        return sessionsList.containsKey(sessionId);
    }

    public void destroySessions() {
        sessionsList = new HashMap<>();
    }

}
