package es.httpserver.controllers;

import es.httpserver.common.Constants;
import es.httpserver.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by User: admin
 * Date: 17/10/2017
 * Time: 16:51
 */
public class SessionControllerTest {

    IUser testuser;
    IWebSession testsession;

    @Before public void setUp() throws Exception {

        testuser = new User();
        testuser.setUsername("testuser");
        testuser.setPassword("12345");
        Vector<UserRole> roles = new Vector<>();
        roles.add(UserRole.PAGE_1);
        testuser.setRoles(roles);
        testsession = new WebSession("TESTSESION", testuser);

    }

    @Test public void getInstance() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        // when
        // then
        assertNotNull(controladorDeSesiones);

    }

    @Test public void getSessionInfo() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.addSession(testsession);
        // when
        IWebSession session = controladorDeSesiones.getSession(testsession.getId());
        // then
        assertThat(session, is(equalTo(testsession)));

    }

    @Test public void addSessionInfo() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addSession(testsession);
        // then
        assertThat(controladorDeSesiones.getSession(testsession.getId()), is(equalTo(testsession)));

    }

    @Test public void addNoLoginSessionInfo() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addNoLoginSession("NO_LOGIN_USER", Constants.PAGE_1_PARAMETER);
        // then
        assertThat(controladorDeSesiones.getSession("NO_LOGIN_USER").getUser().getUsername(), is(equalTo(null)));
        assertThat(controladorDeSesiones.getSession("NO_LOGIN_USER").getReferer(), is(equalTo(Constants.PAGE_1_PARAMETER)));

    }

    @Test public void removeSessionInfo() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addSession(testsession);
        controladorDeSesiones.removeSession(testsession.getId());
        // then
        assertThat(controladorDeSesiones.getSession(testsession.getId()), is(equalTo(null)));

    }

    @Test public void getSessionUserName() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addSession(testsession);
        // then
        assertThat(controladorDeSesiones.getSession(testsession.getId()).getUser().getUsername(), is(equalTo("testuser")));

    }

    @Test public void isExpired() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addSession(testsession);
        // then
        assertThat(controladorDeSesiones.isExpired("TESTSESION"), is(equalTo(false)));
        Thread.sleep(30010);
        assertThat(controladorDeSesiones.isExpired("TESTSESION"), is(equalTo(true)));

    }

    @Test public void existSessionId() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addSession(testsession);
        // then
        assertThat(controladorDeSesiones.existSessionId("TESTSESION"), is(equalTo(true)));

    }

    @Test public void destroySessions() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        // when
        controladorDeSesiones.addSession(testsession);
        controladorDeSesiones.destroySessions();
        // then
        assertThat(controladorDeSesiones.existSessionId("TESTSESION"), is(equalTo(false)));

    }

}