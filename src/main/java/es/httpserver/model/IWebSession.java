package es.httpserver.model;

/**
 * Created by User: admin
 * Date: 19/10/2017
 * Time: 12:39
 */
public interface IWebSession {

    String getId();

    IUser getUser();

    void setUser(IUser user);

    String getReferer();

    void setReferer(String referer);

    void logout();

    boolean isExpired(int millisecondsToExpire);
}
