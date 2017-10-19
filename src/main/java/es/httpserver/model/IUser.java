package es.httpserver.model;

import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * Created by User: admin
 * Date: 19/10/2017
 * Time: 12:40
 */
public interface IUser {
    String getUsername();

    void setUsername(String username);

    List<String> getRoles();

    void setRoles(List<String> roles);

    @XmlTransient boolean isCorrectPassword(String password);

    void setPassword(String password);

    @Override String toString();

    String toJson();

    String toXml();
}
