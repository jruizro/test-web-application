package es.httpserver.model;

import java.util.Optional;

/**
 * Created by User: admin
 * Date: 19/10/2017
 * Time: 14:46
 */
public enum UserRole {

    PAGE_1("PAGE_1"),
    PAGE_2("PAGE_2"),
    PAGE_3("PAGE_3"),
    ADMIN("ADMIN");

    private final String value;

    UserRole(String roleInString) {
        this.value = roleInString;
    }

    public String getString() {
        return this.value;
    }

    public static Optional<UserRole> getFromString(String roleValue) {
        if (roleValue != null) {
            for (UserRole role : UserRole.values()) {
                if (roleValue.equalsIgnoreCase(role.getString())) {
                    return Optional.of(role);
                }
            }
        }
        return Optional.empty();
    }

    @Override public String toString() {
        return getString();
    }
}
