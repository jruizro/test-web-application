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
public class User {

    private String username;
    private List<String> roles = new Vector<>();

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @XmlTransient public boolean isCorrectPassword(String password) {
        return this.password.equals(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override public String toString() {
        return new StringBuilder("Username: '").append(username).append("' -> Roles: ").append(Arrays.toString(roles.toArray())).toString();
    }

    public String toJson() {

        JsonArrayBuilder rolesJson = Json.createArrayBuilder();
        for (String rol : roles) {
            rolesJson.add(Json.createObjectBuilder().add("rol", rol));
        }
        JsonObjectBuilder usuarioJson = Json.createObjectBuilder().add("username", username).add("roles", rolesJson.build().toString());
        return usuarioJson.build().toString();
    }

    public String toXml() {

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
