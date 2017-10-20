package es.httpserver.controllers;

import es.httpserver.common.Constants;
import es.httpserver.model.IUser;
import es.httpserver.model.IWebSession;
import es.httpserver.model.WebSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 12:40
 */
public class SessionController {

    private static final Logger logger = LogManager.getLogger(SessionController.class.getName());

    private static SessionController instance = null;

    private int sessionTimeout = 300000; // por defecto 5 minutos

    private Map<String, IWebSession> sessionsList = null;

    public static SessionController getInstance() {
        if (instance == null) {
            instance = new SessionController();
        }
        return instance;
    }

    private SessionController() {
        sessionsList = new HashMap<>();
        logger.debug("Session controller started with " + sessionsList.size() + " sessions");

        try {

            ResourceBundle serverConfig = ResourceBundle.getBundle(Constants.SERVER_CONFIG_FILE);
            logger.debug("Loading server configuration from " + Constants.SERVER_CONFIG_FILE + ".properties");

            sessionTimeout = Integer.parseInt(serverConfig.getString(Constants.PROPERTY_SESSION_TIMEOUT));

        } catch (Exception e) {
            logger.error("ERROR loading server configuration: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public IWebSession getSession(String sessionId) {
        return sessionsList.get(sessionId);
    }

    public IWebSession addSession(IWebSession infoDeSesion) {
        sessionsList.put(infoDeSesion.getId(), infoDeSesion);
        logger.debug("Stored session '" + infoDeSesion.getId() + "' from user '" + infoDeSesion.getUser().getUsername() + "'");
        return infoDeSesion;
    }

    public IWebSession addNoLoginSession(String sessionId, String paginaSolicitada) {
        IWebSession infoDeSesion = new WebSession(sessionId);
        infoDeSesion.setReferer(paginaSolicitada);
        sessionsList.put(sessionId, infoDeSesion);
        logger.debug("Stored session '" + infoDeSesion.getId() + "' for NO LOGGED user -> " + paginaSolicitada);
        return infoDeSesion;
    }

    public IWebSession updateUserSession(String sessionId, IUser userYaLogado) {

        IWebSession sessionSinUser = sessionsList.get(sessionId);
        sessionSinUser.setUser(userYaLogado);
        logger.debug("Session " + sessionId + " assigned to user " + userYaLogado.getUsername());

        return sessionSinUser;
    }

    public String removeSession(String sessionId) {

        String ususarioDeLaSession = "";
        if (sessionsList.containsKey(sessionId)) {
            ususarioDeLaSession = sessionsList.get(sessionId).getUser().getUsername();
            logger.debug("Removed session '" + sessionId + "' of user '" + ususarioDeLaSession + "'");
            sessionsList.remove(sessionId);
        }
        return ususarioDeLaSession;
    }

    public boolean isExpired(String sessionId) {
        return sessionsList.get(sessionId).isExpired(sessionTimeout);
    }

    public boolean existSessionId(String sessionId) {
        return sessionsList.containsKey(sessionId);
    }

    public void destroySessions() {
        sessionsList = new HashMap<>();
    }

}
