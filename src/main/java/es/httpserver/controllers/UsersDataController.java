package es.httpserver.controllers;

import es.httpserver.common.Constants;
import es.httpserver.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 13:11
 */
public class UsersDataController {

    private static final Logger logger = LogManager.getLogger(UsersDataController.class.getName());

    private static UsersDataController instance = new UsersDataController();

    private HashMap<String, User> listaUsuarios = new HashMap<>();


    public static UsersDataController getInstance() {
        if (instance == null) {
            instance = new UsersDataController();
        }
        return instance;
    }

    private UsersDataController() {

        try {

            ResourceBundle usersRolesConfig = ResourceBundle.getBundle(Constants.USERS_CONFIG_FILE);
            logger.debug("Cargando configuraci�n de Usuarios-Roles desde " + Constants.USERS_CONFIG_FILE + ".properties");

            if (usersRolesConfig != null) {

                List<String> listaDeUsuarios = readResourceWithCommas(usersRolesConfig.getString(Constants.PROPERTY_USERS_LIST));

                for (String nombreUsuario : listaDeUsuarios) {
                    User usuario = new User();
                    usuario.setUsername(nombreUsuario);
                    usuario.setPassword(usersRolesConfig.getString(nombreUsuario + Constants.PROPERTY_USER_PASS));
                    usuario.setRoles(readResourceWithCommas(usersRolesConfig.getString(nombreUsuario + Constants.PROPERTY_USER_ROLE)));
                    logger.debug("Leida configuracion del usuario: " + usuario.toString());
                    listaUsuarios.put(usuario.getUsername(), usuario);
                }

            } else {
                logger.error("ERROR al cargar el fichero de configuracion de Usuarios-Roles!");
            }
            logger.debug("Leida configuraci�n de " + listaUsuarios.size() + " usuarios");

        } catch (Exception e) {
            logger.error("ERROR al cargar el fichero de configuracion de Usuarios-Roles : " + e.getMessage());
            e.printStackTrace(System.out);
        }


    }

    public User addUser(User newUser) {
        return listaUsuarios.put(newUser.getUsername(), newUser);
    }

    public User updateUser(User updateUser) {
        return listaUsuarios.put(updateUser.getUsername(), updateUser);
    }

    public User deleteUser(String userToDelete) {
        return listaUsuarios.remove(userToDelete);
    }

    public boolean checkUserPassword(String username, String password) {

        boolean isValidUSer = false;
        if (!listaUsuarios.isEmpty()) {
            isValidUSer = listaUsuarios.containsKey(username) && listaUsuarios.get(username).isCorrectPassword(password);
        }

        return isValidUSer;
    }

    public User getUser(String username) {
        logger.debug("Buscando info del Usuario: " + username);
        return listaUsuarios.getOrDefault(username, null);
    }

    public HashMap<String, User> getUsers() {
        return listaUsuarios;
    }

    public void deleteAllUsers() {
        logger.debug("Se van borrar " + listaUsuarios.size() + " usuarios");
        listaUsuarios = new HashMap<>();
    }

    private List<String> readResourceWithCommas(String listaConComas) {
        List<String> listaSeparadaSinComas = new Vector<>();
        StringTokenizer serviceTokenizer = new StringTokenizer(listaConComas, ",");
        while (serviceTokenizer.hasMoreElements()) {
            listaSeparadaSinComas.add(serviceTokenizer.nextToken().trim());
        }
        return listaSeparadaSinComas;
    }

}
