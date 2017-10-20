package es.httpserver.controllers;

import es.httpserver.common.Constants;
import es.httpserver.model.IWebSession;
import es.httpserver.model.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by User: admin
 * Date: 19/10/2017
 * Time: 12:23
 */
public class NavigationController {

    private static final Logger logger = LogManager.getLogger(NavigationController.class.getName());

    public boolean hasAccessToPage(IWebSession userSession, String paginaDestino) {

        boolean tienePermiso;

        logger.debug("Session '" + userSession.getId() + "' needs access to '" + paginaDestino + "'");

        List<UserRole> userListRoles = userSession.getUser().getRoles();

        switch (paginaDestino) {
            case Constants.PAGE_1_PARAMETER:
                tienePermiso = userListRoles.contains(UserRole.PAGE_1) || userListRoles.contains(UserRole.ADMIN);
                break;
            case Constants.PAGE_2_PARAMETER:
                tienePermiso = userListRoles.contains(UserRole.PAGE_2) || userListRoles.contains(UserRole.ADMIN);
                break;
            case Constants.PAGE_3_PARAMETER:
                tienePermiso = userListRoles.contains(UserRole.PAGE_3) || userListRoles.contains(UserRole.ADMIN);
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

    public String getPagePath(String paginaSolicitada) {

        switch (paginaSolicitada) {
            case Constants.PAGE_1_PARAMETER:
                return Constants.PAGE_1_PATH;
            case Constants.PAGE_2_PARAMETER:
                return Constants.PAGE_2_PATH;
            case Constants.PAGE_3_PARAMETER:
                return Constants.PAGE_3_PATH;
            default:
                return Constants.HOME_PAGE_PATH;
        }
    }


}
