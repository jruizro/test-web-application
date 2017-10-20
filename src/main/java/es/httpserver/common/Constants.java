package es.httpserver.common;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 17:05
 */
public class Constants {

    public static final String LOGIN_PAGE_PATH = "/web/login.html";
    public static final String HOME_PAGE_PATH = "/web/home.html";
    public static final String PAGE_1_PATH = "/web/page_1.html";
    public static final String PAGE_2_PATH = "/web/page_2.html";
    public static final String PAGE_3_PATH = "/web/page_3.html";
    public static final String ERROR_PAGE_PATH = "/web/error.html";

    public static final String PAGE_1_PARAMETER = "PAGE_1";
    public static final String PAGE_2_PARAMETER = "PAGE_2";
    public static final String PAGE_3_PARAMETER = "PAGE_3";

    public static final String WEB_USERNAME_PATTERN = "#$USERNAME$#";
    public static final String WEB_ERROR_PATTERN = "#$ERROR$#";

    public static final String MIME_FORMAT_JSON = "application/json";
    public static final String MIME_FORMAT_XML = "application/xml";
    public static final String MIME_FORMAT_XML_2 = "application/xhtml+xml";
    public static final String MIME_FORMAT_HTML = "text/html";
    public static final String MIME_FORMAT_TEXT = "text/plain";
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String REQ_PARAM_ROLES = "roles";
    public static final String REQ_PARAM_USERNAME = "username";
    public static final String REQ_PARAM_PASSWORD = "password";

    public static final String HEADER_COOKIE = "Cookie";
    public static final String HEADER_SET_COOKIE = "Set-Cookie";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ACCEPT = "Accept";

    public static final String PAGE_PARAM = "pag";

    public static final String SERVER_CONFIG_FILE = "serverconfig";
    public static final String USERS_CONFIG_FILE = "users";

    public static final String PROPERTY_SESSION_TIMEOUT = "http.session.timeout";
    public static final String PROPERTY_USERS_LIST = "users.names.list";
    public static final String PROPERTY_USER_PASS = ".pass";
    public static final String PROPERTY_USER_ROLE = ".role";


}
