package es.httpserver.model;

import java.util.Optional;

/**
 * Created by User: admin
 * Date: 20/10/2017
 * Time: 9:23
 */
public enum ContextPath {

    ROOT("/"),
    LOGIN("/login"),
    LOGOUT("/logout"),
    WEB("/web"),
    USERS_WS("/users");

    private final String stringPath;

    ContextPath(String stringPath) {
        this.stringPath = stringPath;
    }

    public String getStringPath() {
        return this.stringPath;
    }

    public static Optional<ContextPath> getFromString(String stringPath) {
        if (stringPath != null) {
            for (ContextPath contextPath : ContextPath.values()) {
                if (stringPath.equalsIgnoreCase(contextPath.getStringPath())) {
                    return Optional.of(contextPath);
                }
            }
        }
        return Optional.empty();
    }

    @Override public String toString() {
        return getStringPath();
    }

}
