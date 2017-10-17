package es.httpserver.controllers;

import es.httpserver.model.User;

import java.util.*;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 13:11
 */
public class UsersDataController {

    private static UsersDataController instance = new UsersDataController();

    private HashMap<String, User> listaUsuarios = new HashMap<>();


    public static UsersDataController getInstance() {
        if (instance == null) {
            instance = new UsersDataController();
        }
        return instance;
    }

    private UsersDataController() {

        ResourceBundle usersRolesConfig = null;

        try {

            usersRolesConfig = ResourceBundle.getBundle("users");
            System.out.println("Cargando configuración de Usuarios-Roles desde " + "users" + ".properties");

        } catch (Exception e) {
            System.out.println("ERROR al cargar el fichero de configuracion de Usuarios-Roles : " + e.getMessage());
            e.printStackTrace(System.out);
        }

        if (usersRolesConfig != null) {

            List<String> listaDeUsuarios = readResourceWithCommas(usersRolesConfig.getString("users.names.list"));
            Iterator iteradorListaDeUsuarios = listaDeUsuarios.iterator();
            while (iteradorListaDeUsuarios.hasNext()) {
                String nombreUsuario = (String) iteradorListaDeUsuarios.next();
                User usuario = new User();
                usuario.setUsername(nombreUsuario);
                usuario.setPassword(usersRolesConfig.getString(nombreUsuario + ".pass"));
                usuario.setRoles(readResourceWithCommas(usersRolesConfig.getString(nombreUsuario + ".role")));
                System.out.println("Leida configuracion del usuario: " + usuario.toString());
                listaUsuarios.put(usuario.getUsername(), usuario);
            }
        } else {
            System.out.println("ERROR al cargar el fichero de configuracion de Usuarios-Roles!");
        }
        System.out.println("Leida configuración de " + listaUsuarios.size() + " usuarios");
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
        System.out.println("Buscando info del Usuario: " + username);
        return listaUsuarios.containsKey(username) ? listaUsuarios.get(username) : null;
    }

    public HashMap<String, User> getUsers() {
        return listaUsuarios;
    }

    public void deleteAllUsers() {
        System.out.println("Se van borrar " + listaUsuarios.size() + " usuarios");
        listaUsuarios = new HashMap<>();
    }

    private List<String> readResourceWithCommas(String listaConComas) {
        List<String> listaSeparadaSinComas = new Vector<String>();
        StringTokenizer serviceTokenizer = new StringTokenizer(listaConComas, ",");
        while (serviceTokenizer.hasMoreElements()) {
            listaSeparadaSinComas.add(serviceTokenizer.nextToken().trim());
        }
        return listaSeparadaSinComas;
    }

}
