package es.httpserver.model;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 12:39
 */
public class WebSession implements IWebSession {

    private String id;
    private IUser user;
    private String referer = null;

    private long lastAction = System.currentTimeMillis();

    public WebSession(String id) {
        this.id = id;
        this.user = new User();
        lastAction = System.currentTimeMillis();
    }

    public WebSession(String id, IUser user) {
        this.id = id;
        this.user = user;
        lastAction = System.currentTimeMillis();
    }

    @Override public String getId() {
        lastAction = System.currentTimeMillis();
        return id;
    }

    @Override public IUser getUser() {
        lastAction = System.currentTimeMillis();
        return user;
    }

    @Override public void setUser(IUser user) {
        lastAction = System.currentTimeMillis();
        this.user = user;
    }

    @Override public String getReferer() {
        lastAction = System.currentTimeMillis();
        return referer;
    }

    @Override public void setReferer(String referer) {
        this.referer = referer;
    }

    @Override public void logout() {
        this.user = null;
        referer = null;
    }

    @Override public boolean isExpired(int millisecondsToExpire) {
        return (System.currentTimeMillis() - lastAction) > millisecondsToExpire;
    }


}
