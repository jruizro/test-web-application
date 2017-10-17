package es.httpserver.authentication;

import com.sun.net.httpserver.BasicAuthenticator;
import es.httpserver.controllers.UsersDataController;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 11:35
 */
public class BasicAuthenticationChecker extends BasicAuthenticator {

    public BasicAuthenticationChecker(String s) {
        super(s);
    }

    @Override public boolean checkCredentials(String username, String password) {
        System.out.println("checkCredentials - [" + username + " : " + password + "]");
        return UsersDataController.getInstance().checkUserPassword(username, password);
    }
}
