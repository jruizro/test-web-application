package es.httpserver.controllers;

import es.httpserver.common.Constants;
import es.httpserver.model.User;
import es.httpserver.model.WebSession;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static java.lang.Thread.sleep;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by User: admin
 * Date: 17/10/2017
 * Time: 16:51
 */
public class SessionControllerTest {

    User testuser;
    WebSession testsession;

    @Before public void setUp() throws Exception {

        testuser = new User();
        testuser.setUsername("testuser");
        testuser.setPassword("12345");
        Vector<String> roles = new Vector<>();
        roles.add(Constants.ROLE_4_PAGE_1);
        testuser.setRoles(roles);
        testsession = new WebSession("TESTSESION" , testuser);

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
        controladorDeSesiones.addSessionInfo(testsession);
        // when
        WebSession session = controladorDeSesiones.getSessionInfo(testsession.getId());
        // then
        assertThat(session, is(equalTo(testsession)));

    }

    @Test public void addSessionInfo() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addSessionInfo(testsession);
        // then
        assertThat(controladorDeSesiones.getSessionInfo(testsession.getId()), is(equalTo(testsession)));

    }

    @Test public void addNoLoginSessionInfo() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addNoLoginSessionInfo("NO_LOGIN_USER", Constants.PAGE_1_PARAMETER);
        // then
        assertThat(controladorDeSesiones.getSessionUserName("NO_LOGIN_USER"), is(equalTo(null)));
        assertThat(controladorDeSesiones.getSessionInfo("NO_LOGIN_USER").getNextPage(), is(equalTo(Constants.PAGE_1_PARAMETER)));

    }

    @Test public void removeSessionInfo() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addSessionInfo(testsession);
        controladorDeSesiones.removeSessionInfo(testsession.getId());
        // then
        assertThat(controladorDeSesiones.getSessionInfo(testsession.getId()), is(equalTo(null)));

    }

    @Test public void hasAccessToPage() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addSessionInfo(testsession);
        // then
        assertThat(controladorDeSesiones.hasAccessToPage(testsession.getId(), Constants.PAGE_1_PARAMETER), is(equalTo(true)));
        assertThat(controladorDeSesiones.hasAccessToPage(testsession.getId(), Constants.PAGE_3_PARAMETER), is(equalTo(false)));

    }

    @Test public void getSessionUserName() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addSessionInfo(testsession);
        // then
        assertThat(controladorDeSesiones.getSessionUserName(testsession.getId()), is(equalTo("testuser")));

    }

    @Test public void isExpired() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        controladorDeSesiones.destroySessions();
        // when
        controladorDeSesiones.addSessionInfo(testsession);
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
        controladorDeSesiones.addSessionInfo(testsession);
        // then
        assertThat(controladorDeSesiones.existSessionId("TESTSESION"), is(equalTo(true)));

    }

    @Test public void destroySessions() throws Exception {

        // given
        SessionController controladorDeSesiones = SessionController.getInstance();
        // when
        controladorDeSesiones.addSessionInfo(testsession);
        controladorDeSesiones.destroySessions();
        // then
        assertThat(controladorDeSesiones.existSessionId("TESTSESION"), is(equalTo(false)));

    }

}