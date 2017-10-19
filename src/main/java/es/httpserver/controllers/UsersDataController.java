package es.httpserver.controllers;

import es.httpserver.dao.UsersDAO;
import es.httpserver.model.IUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 13:11
 */
public class UsersDataController {

    private static final Logger logger = LogManager.getLogger(UsersDataController.class.getName());

    private static UsersDataController instance = new UsersDataController();

    private HashMap<String, IUser> listaUsuarios = new HashMap<>();


    public static UsersDataController getInstance() {
        if (instance == null) {
            instance = new UsersDataController();
        }
        return instance;
    }

    private UsersDataController() {

        listaUsuarios = new UsersDAO().getUsers();

    }

    public IUser addUser(IUser newUser) {
        return listaUsuarios.put(newUser.getUsername(), newUser);
    }

    public IUser updateUser(IUser updateUser) {
        return listaUsuarios.put(updateUser.getUsername(), updateUser);
    }

    public IUser deleteUser(String userToDelete) {
        return listaUsuarios.remove(userToDelete);
    }

    public boolean checkUserPassword(String username, String password) {

        boolean isValidUSer = false;
        if (!listaUsuarios.isEmpty()) {
            isValidUSer = listaUsuarios.containsKey(username) && listaUsuarios.get(username).isCorrectPassword(password);
        }

        return isValidUSer;
    }

    public IUser getUser(String username) {
        logger.debug("Buscando info del Usuario: " + username);
        return listaUsuarios.getOrDefault(username, null);
    }

    public HashMap<String, IUser> getUsers() {
        return listaUsuarios;
    }

    public void deleteAllUsers() {
        logger.debug("Se van borrar " + listaUsuarios.size() + " usuarios");
        listaUsuarios = new HashMap<>();
    }

}
