package es.httpserver.model;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by User: admin
 * Date: 10/10/2017
 * Time: 11:28
 */
@XmlRootElement
public class User implements IUser {

    private String username;
    private List<UserRole> roles = new Vector<>();

    private String password;

    @Override public String getUsername() {
        return username;
    }

    @Override public void setUsername(String username) {
        this.username = username;
    }

    @Override public List<UserRole> getRoles() {
        return roles;
    }

    @Override public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    @Override @XmlTransient public boolean isCorrectPassword(String password) {
        return this.password.equals(password);
    }

    @Override public void setPassword(String password) {
        this.password = password;
    }

    @Override public String toString() {
        return new StringBuilder("Username: '").append(username).append("' -> Roles: ").append(Arrays.toString(roles.toArray())).toString();
    }

    @Override public String toJson() {

        JsonArrayBuilder rolesJson = Json.createArrayBuilder();
        for (UserRole rol : roles) {
            rolesJson.add(Json.createObjectBuilder().add("rol", rol.getString()));
        }
        JsonObjectBuilder usuarioJson = Json.createObjectBuilder().add("username", username).add("roles", rolesJson.build().toString());
        return usuarioJson.build().toString();
    }

    @Override public String toXml() {

        JAXBContext contexto = null;
        String requestToXML = null;
        try {
            contexto = JAXBContext.newInstance(User.class);
            Marshaller marshaller = contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            OutputStream output = new OutputStream() {
                private StringBuilder string = new StringBuilder();

                @Override public void write(int b) throws IOException {
                    this.string.append((char) b);
                }

                @Override public String toString() {
                    return this.string.toString();
                }
            };
            marshaller.marshal(this, output);
            requestToXML = output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestToXML;

    }
}
