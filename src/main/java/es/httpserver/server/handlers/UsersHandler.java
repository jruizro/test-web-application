package es.httpserver.server.handlers;

import com.sun.net.httpserver.Headers;
import es.httpserver.common.Constants;
import es.httpserver.model.ContextPath;
import es.httpserver.model.IUser;
import es.httpserver.model.User;
import es.httpserver.model.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static es.httpserver.common.Utils.splitFiledsWithDelimeter;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 11:24
 */
public class UsersHandler extends HTTPCommonHandler {

    private static final Logger logger = LogManager.getLogger(UsersHandler.class.getName());

    /**
     * GET /users/{username} - return user info
     *
     * @throws IOException
     */
    public void get() throws IOException {

        logger.debug("UsersHandler - get -> Read User");

        List<String> uriParameters = exchangeContext.getRequestUriPath(ContextPath.USERS_WS.getStringPath());
        IUser userRequested = getUsersDataController().getUser(uriParameters.get(0));

        if (userRequested != null) {
            sendRESTSuccessfulResponse(userRequested);
        } else {
            sendNotFoundResponse();
        }

    }

    /**
     * POST /users
     * parameters:
     * username > name
     * password > password
     * roles > role1,role2,role3
     *
     * @throws IOException
     */
    public void post() throws IOException {

        logger.debug("UsersHandler - post -> Create User");
        HashMap<String, String> bodyParameters = exchangeContext.getRequestBodyParameters();

        if (bodyParameters.get(Constants.REQ_PARAM_USERNAME) != null && bodyParameters.get(Constants.REQ_PARAM_PASSWORD) != null && bodyParameters.get(Constants.REQ_PARAM_ROLES) != null) {
            IUser newUser = new User();
            newUser.setUsername(bodyParameters.get(Constants.REQ_PARAM_USERNAME));
            newUser.setPassword(bodyParameters.get(Constants.REQ_PARAM_PASSWORD));

            List<UserRole> listaDeRolesUsuario = new Vector<>();
            List<String> rolesEnString = splitFiledsWithDelimeter(bodyParameters.get(Constants.REQ_PARAM_ROLES), ",");
            for (String role: rolesEnString) {
                UserRole.getFromString(role).ifPresent(listaDeRolesUsuario::add);
            }
            newUser.setRoles(listaDeRolesUsuario);

            if (getUsersDataController().getUser(newUser.getUsername()) != null) {
                logger.error("ERROR: user already exists");
                sendBadRequestResponse("ERROR: user already exists");
            } else {
                getUsersDataController().addUser(newUser);
                logger.debug("Added new User: " + getUsersDataController().getUser(newUser.getUsername()).toString());
                sendRESTCreatedResponse(getUsersDataController().getUser(newUser.getUsername()));
            }

        } else {
            logger.error("ERROR: Required parameter is missing");
            sendBadRequestResponse("ERROR: Required parameter is missing");
        }
    }

    /**
     * UPDATE /users/{username}
     * parameters:
     * username > name
     * roles > role1,role2,role3
     *
     * @throws IOException
     */
    public void put() throws IOException {
        logger.debug("UsersHandler - update - Update User");

        List<String> uriParameters = exchangeContext.getRequestUriPath(ContextPath.USERS_WS.getStringPath());

        IUser userToUpdate = getUsersDataController().getUser(uriParameters.get(0));
        if (userToUpdate != null) {

            HashMap<String, String> bodyParameters = exchangeContext.getRequestBodyParameters();
            if (bodyParameters.get(Constants.REQ_PARAM_ROLES) != null) {

                List<UserRole> listaDeRolesUsuario = new Vector<>();
                List<String> rolesEnString = splitFiledsWithDelimeter(bodyParameters.get(Constants.REQ_PARAM_ROLES), ",");
                for (String role: rolesEnString) {
                    UserRole.getFromString(role).ifPresent(listaDeRolesUsuario::add);
                }
                userToUpdate.setRoles(listaDeRolesUsuario);

                IUser userUpdated = getUsersDataController().updateUser(userToUpdate);
                sendRESTSuccessfulResponse(userUpdated);
            } else {
                logger.error("ERROR: Required parameter is missing");
                sendBadRequestResponse("ERROR: Required parameter is missing");
            }

        } else {
            logger.error("ERROR: User to upgrade does NOT exist");
            sendNotFoundResponse();
        }

    }


    /**
     * DELETE /users/{username}
     *
     * @throws IOException
     */
    public void delete() throws IOException {
        logger.debug("UsersHandler - delete -> Delete User");

        List<String> uriParameters = exchangeContext.getRequestUriPath(ContextPath.USERS_WS.getStringPath());
        IUser userDeleted = getUsersDataController().deleteUser(uriParameters.get(0));

        if (userDeleted != null) {
            sendRESTSuccessfulResponse(userDeleted);
        } else {
            sendNotFoundResponse();
        }

    }


    private String generateRESTResponse(IUser userInfo) {

        String responseBody;
        Headers responseHeaders = exchangeContext.getResponseHeaders();
        HashMap<String, String> requestHeaders = exchangeContext.getRequestHeaders();

        String contentType = requestHeaders.get(Constants.HEADER_ACCEPT);
        // Content Negotiation
        if (contentType != null) {
            if (contentType.contains(Constants.MIME_FORMAT_JSON)) {
                responseHeaders.set(Constants.HEADER_CONTENT_TYPE, String.format("%s; charset=%s", Constants.MIME_FORMAT_JSON, Constants.DEFAULT_CHARSET));
                responseBody = userInfo.toJson();
            } else if (contentType.contains(Constants.MIME_FORMAT_XML) || contentType.contains(Constants.MIME_FORMAT_XML_2) || contentType.contains(Constants.MIME_FORMAT_HTML)) {
                responseHeaders.set(Constants.HEADER_CONTENT_TYPE, String.format("%s; charset=%s", Constants.MIME_FORMAT_XML, Constants.DEFAULT_CHARSET));
                responseBody = userInfo.toXml();
            } else {
                responseHeaders.set(Constants.HEADER_CONTENT_TYPE, String.format("%s; charset=%s", Constants.MIME_FORMAT_TEXT, Constants.DEFAULT_CHARSET));
                responseBody = userInfo.toString();
            }
        } else {
            responseHeaders.set(Constants.HEADER_CONTENT_TYPE, String.format("%s; charset=%s", Constants.MIME_FORMAT_TEXT, Constants.DEFAULT_CHARSET));
            responseBody = userInfo.toString();
        }
        return responseBody;
    }


    private void sendRESTSuccessfulResponse(IUser userInfo) throws IOException {
        sendSuccessfulResponse(generateRESTResponse(userInfo));
    }

    private void sendRESTCreatedResponse(IUser user) throws IOException {
        sendCreatedResponse(generateRESTResponse(user));
    }


}