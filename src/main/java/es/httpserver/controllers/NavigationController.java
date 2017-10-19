package es.httpserver.controllers;

import es.httpserver.common.Constants;
import es.httpserver.model.IWebSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by User: admin
 * Date: 19/10/2017
 * Time: 12:23
 */
public class NavigationController {

    private static final Logger logger = LogManager.getLogger(NavigationController.class.getName());

    public boolean hasAccessToPage(IWebSession userSession, String paginaDestino) {

        boolean tienePermiso;

        logger.debug("Session '" + userSession.getId() + "' solicita permiso para '" + paginaDestino + "'");

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

}
