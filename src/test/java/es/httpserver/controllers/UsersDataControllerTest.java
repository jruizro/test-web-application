package es.httpserver.controllers;

import es.httpserver.common.Constants;
import es.httpserver.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Vector;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

/**
 * Created by User: admin
 * Date: 17/10/2017
 * Time: 15:31
 */
public class UsersDataControllerTest {


    User testuser;

    @Before public void setUp() throws Exception {

        testuser = new User();
        testuser.setUsername("testuser");
        testuser.setPassword("12345");
        Vector<String> roles = new Vector<>();
        roles.add(Constants.ROLE_4_PAGE_2);
        testuser.setRoles(roles);

    }

    @After public void tearDown() throws Exception {
    }

    @Test public void getInstance() throws Exception {

        // given
        UsersDataController controladorDeUsuarios = UsersDataController.getInstance();
        // when
        // then
        assertThat(controladorDeUsuarios.getUsers().size(), is(equalTo(5)));

    }

    @Test public void getUsers() throws Exception {
        // given
        UsersDataController controladorDeUsuarios = UsersDataController.getInstance();
        // when
        controladorDeUsuarios.deleteAllUsers();
        controladorDeUsuarios.addUser(testuser);
        // then
        assertThat(controladorDeUsuarios.getUsers().size(), is(equalTo(1)));

    }

    @Test public void addUser() throws Exception {
        // given
        UsersDataController controladorDeUsuarios = UsersDataController.getInstance();
        // when
        controladorDeUsuarios.deleteAllUsers();
        controladorDeUsuarios.addUser(testuser);
        // then
        assertThat(controladorDeUsuarios.getUser(testuser.getUsername()), is(equalTo(testuser)));
    }

    @Test public void updateUser() throws Exception {
        // given
        UsersDataController controladorDeUsuarios = UsersDataController.getInstance();
        controladorDeUsuarios.deleteAllUsers();
        controladorDeUsuarios.addUser(testuser);
        Vector<String> rolesNuevos = new Vector<>();
        rolesNuevos.add(Constants.ROLE_ADMIN);
        testuser.setRoles(rolesNuevos);
        // when
        controladorDeUsuarios.updateUser(testuser);
        // then
        assertThat(controladorDeUsuarios.getUser(testuser.getUsername()).getRoles(), is(equalTo(testuser.getRoles())));
    }

    @Test public void deleteUser() throws Exception {
        // given
        UsersDataController controladorDeUsuarios = UsersDataController.getInstance();
        // when
        controladorDeUsuarios.deleteAllUsers();
        controladorDeUsuarios.addUser(testuser);
        controladorDeUsuarios.deleteUser(testuser.getUsername());
        // then
        assertThat(controladorDeUsuarios.getUser(testuser.getUsername()), is(equalTo(null)));

    }

    @Test public void checkUserPassword() throws Exception {
        // given
        UsersDataController controladorDeUsuarios = UsersDataController.getInstance();
        // when
        controladorDeUsuarios.addUser(testuser);
        // then
        assertThat(controladorDeUsuarios.checkUserPassword("testuser", "12345"), is(equalTo(true)));
    }

    @Test public void getUser() throws Exception {

        // given
        UsersDataController controladorDeUsuarios = UsersDataController.getInstance();
        // when
        controladorDeUsuarios.addUser(testuser);
        // then
        assertThat(controladorDeUsuarios.getUser("testuser"), is(equalTo(testuser)));

    }

    @Test public void deleteAllUsers() throws Exception {
        // given
        UsersDataController controladorDeUsuarios = UsersDataController.getInstance();
        // when
        controladorDeUsuarios.deleteAllUsers();
        // then
        assertThat(controladorDeUsuarios.getUsers().size(), is(equalTo(0)));
    }


}