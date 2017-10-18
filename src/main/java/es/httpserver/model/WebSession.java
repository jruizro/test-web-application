package es.httpserver.model;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 12:39
 */
public class WebSession {

    private String id;
    private User user;
    private String nextPage = null;

    private long lastAction = System.currentTimeMillis();

    public WebSession(String id, User user) {
        this.id = id;
        this.user = user;
        lastAction = System.currentTimeMillis();
    }

    public String getId() {
        lastAction = System.currentTimeMillis();
        return id;
    }

    public User getUser() {
        lastAction = System.currentTimeMillis();
        return user;
    }

    public void setUser(User user) {
        lastAction = System.currentTimeMillis();
        this.user = user;
    }

    public String getNextPage() {
        lastAction = System.currentTimeMillis();
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public void logout() {
        this.user = null;
        nextPage = null;
    }

    public boolean isExpired(int millisecondsToExpire) {
        return (System.currentTimeMillis() - lastAction) > millisecondsToExpire;
    }


}
