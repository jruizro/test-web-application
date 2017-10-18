package es.httpserver.authentication;

import com.sun.net.httpserver.BasicAuthenticator;
import es.httpserver.controllers.UsersDataController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 11:35
 */
public class BasicAuthenticationChecker extends BasicAuthenticator {

    private static final Logger logger = LogManager.getLogger(BasicAuthenticationChecker.class.getName());

    public BasicAuthenticationChecker(String s) {
        super(s);
    }

    @Override public boolean checkCredentials(String username, String password) {
        logger.debug("checkCredentials - [" + username + " : " + password + "]");
        return UsersDataController.getInstance().checkUserPassword(username, password);
    }
}
