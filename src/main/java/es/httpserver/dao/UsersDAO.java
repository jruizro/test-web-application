package es.httpserver.dao;

import es.httpserver.common.Constants;
;
import es.httpserver.model.IUser;
import es.httpserver.model.User;
import es.httpserver.model.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import static es.httpserver.common.Utils.splitFiledsWithDelimeter;

/**
 * Lee la información de los usuarios
 * Created by User: admin
 * Date: 19/10/2017
 * Time: 14:13
 */
public class UsersDAO {

    private static final Logger logger = LogManager.getLogger(UsersDAO.class.getName());

    public HashMap<String, IUser> getUsers() {

        HashMap<String, IUser> listaUsuarios = new HashMap<>();

        try {

            ResourceBundle usersRolesConfig = ResourceBundle.getBundle(Constants.USERS_CONFIG_FILE);
            logger.debug("Cargando configuración de Usuarios-Roles desde " + Constants.USERS_CONFIG_FILE + ".properties");

            if (usersRolesConfig != null) {

                List<String> listaDeUsuarios = splitFiledsWithDelimeter(usersRolesConfig.getString(Constants.PROPERTY_USERS_LIST), ",");

                for (String nombreUsuario : listaDeUsuarios) {
                    IUser usuario = new User();
                    usuario.setUsername(nombreUsuario);
                    usuario.setPassword(usersRolesConfig.getString(nombreUsuario + Constants.PROPERTY_USER_PASS));
                    List<UserRole> listaDeRolesUsuario = new Vector<>();

                    List<String> rolesEnString = splitFiledsWithDelimeter(usersRolesConfig.getString(nombreUsuario + Constants.PROPERTY_USER_ROLE), ",");
                    for (String role: rolesEnString) {
                        UserRole.getFromString(role).ifPresent(listaDeRolesUsuario::add);
                    }

                    usuario.setRoles(listaDeRolesUsuario);
                    logger.debug("Leida configuracion del usuario: " + usuario.toString());
                    listaUsuarios.put(usuario.getUsername(), usuario);
                }

            } else {
                logger.error("ERROR al cargar el fichero de configuracion de Usuarios-Roles!");
            }
            logger.debug("Leida configuración de " + listaUsuarios.size() + " usuarios");

        } catch (Exception e) {
            logger.error("ERROR al cargar el fichero de configuracion de Usuarios-Roles : " + e.getMessage());
            e.printStackTrace(System.out);
        }
        return listaUsuarios;

    }

}
