package es.httpserver.server.handlers;

import com.sun.net.httpserver.Headers;
import es.httpserver.common.Constants;
import es.httpserver.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 11:24
 */
public class UsersHandler extends HTTPCommonHandler {

    /**
     * GET /users/{username} - return user info
     *
     * @throws IOException
     */
    public void get() throws IOException {

        System.out.println("UsersHandler - get -> Read User");

        List<String> uriParameters = getRequestUriPath(Constants.USERS_CONTEXTPATH);
        User userRequested = getUsersDataController().getUser(uriParameters.get(0));

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

        System.out.println("UsersHandler - post -> Create User");
        HashMap<String, String> bodyParameters = getRequestBodyParameters();

        if (bodyParameters.get("username") != null && bodyParameters.get("password") != null && bodyParameters.get("roles") != null) {
            User newUser = new User();
            newUser.setUsername(bodyParameters.get("username"));
            newUser.setPassword(bodyParameters.get("password"));
            newUser.setRoles(separaCamposPorComas(bodyParameters.get("roles")));

            if (getUsersDataController().getUser(newUser.getUsername()) != null) {
                System.out.println("ERROR: El usuario ya existe");
                sendBadRequestResponse("ERROR: El usuario ya existe");
            } else {
                getUsersDataController().addUser(newUser);
                System.out.println("Added new User: " + getUsersDataController().getUser(newUser.getUsername()).toString());
                sendRESTCreatedResponse(getUsersDataController().getUser(newUser.getUsername()));
            }

        } else {
            System.out.println("ERROR: Falta parametro obligatorio");
            sendBadRequestResponse("ERROR: Falta parametro obligatorio");
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
        System.out.println("UsersHandler - update - Update User");

        List<String> uriParameters = getRequestUriPath(Constants.USERS_CONTEXTPATH);

        User userToUpdate = getUsersDataController().getUser(uriParameters.get(0));
        if (userToUpdate != null) {

            HashMap<String, String> bodyParameters = getRequestBodyParameters();
            if (bodyParameters.get("roles") != null) {
                userToUpdate.setRoles(separaCamposPorComas(bodyParameters.get("roles")));
                User userUpdated = getUsersDataController().updateUser(userToUpdate);
                sendRESTSuccessfulResponse(userUpdated);
            } else {
                System.out.println("ERROR: Falta parametro obligatorio");
                sendBadRequestResponse("ERROR: Falta parametro obligatorio");
            }

        } else {
            System.out.println("ERROR: El usuario a actualizar NO existe");
            sendNotFoundResponse();
        }

    }


    /**
     * DELETE /users/{username}
     *
     * @throws IOException
     */
    public void delete() throws IOException {
        System.out.println("UsersHandler - delete -> Delete User");

        List<String> uriParameters = getRequestUriPath(Constants.USERS_CONTEXTPATH);
        User userDeleted = getUsersDataController().deleteUser(uriParameters.get(0));

        if (userDeleted != null) {
            sendRESTSuccessfulResponse(userDeleted);
        } else {
            sendNotFoundResponse();
        }

    }


    private String generateRESTResponse(User userInfo) {

        String responseBody = "";
        Headers responseHeaders = getResponseHeaders();
        HashMap<String, String> requestHeaders = getRequestHeaders();

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


    private void sendRESTSuccessfulResponse(User userInfo) throws IOException {
        sendSuccessfulResponse(generateRESTResponse(userInfo));
    }

    private void sendRESTCreatedResponse(User user) throws IOException {
        sendCreatedResponse(generateRESTResponse(user));
    }


}