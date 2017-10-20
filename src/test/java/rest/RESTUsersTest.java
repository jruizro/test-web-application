package rest;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RESTUsersTest {

    private String adminBasicAuthorizationEncoded = new String(Base64.encodeBase64(("admin:admin").getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

    @BeforeClass
    public static void setup() throws IOException {
        // Server must be running
    }

    @AfterClass
    public static void tearDown() {  }

    @Test
    public void failAccess() {
        HttpResponse response = request("users/user1", "POST", null, null);
        assertEquals(401,response.getStatusLine().getStatusCode());
    }

    @Test
    public void failWithWrongParameters() {
        Map<String, String> headers = new HashMap<>();

        //Basic Authentication for user "admintest"
        headers.put("Authorization","Basic " + adminBasicAuthorizationEncoded);
        HttpResponse response = request("users/user2", "POST", null, headers);
        assertEquals(400,response.getStatusLine().getStatusCode());
    }

    @Test
    public void getUser() throws IOException {
        Map<String, String> headers = new HashMap<>();
        //Basic Authentication for user "admintest"
        headers.put("Authorization","Basic " + adminBasicAuthorizationEncoded);
        HttpResponse response = request("users/admin", "GET", null , headers);

        assertEquals(200,response.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(response.getEntity()).contains("Username: 'admin' -> Roles: [ADMIN]"));
    }

    @Test
    public void CreateUser() throws IOException {
        Map<String, String> headers = new HashMap<>();

        //Basic Authentication for user "admintest"
        headers.put("Authorization","Basic " + adminBasicAuthorizationEncoded);
        HttpResponse response = request("users", "POST", "username=newone&password=12345&roles=PAGE_1,PAGE_2" , headers);

        assertEquals(201,response.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(response.getEntity()).contains("Username: 'newone' -> Roles: [PAGE_1, PAGE_2]"));
    }

    @Test
    public void DeleteUser() {
        Map<String, String> headers = new HashMap<>();

        //Basic Authentication for user "admintest"
        headers.put("Authorization","Basic " + adminBasicAuthorizationEncoded);
        HttpResponse response = request("users/user1", "DELETE", null, headers);

        assertEquals(200,response.getStatusLine().getStatusCode());
    }

    @Test
    public void UpdateUser() throws IOException {
        Map<String, String> headers = new HashMap<>();

        //Basic Authentication for user "admintest"
        headers.put("Authorization","Basic " + adminBasicAuthorizationEncoded);
        HttpResponse response = request("users/user2", "PUT", "roles=PAGE_3", headers);

        assertEquals(200,response.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(response.getEntity()).contains("Username: 'user2' -> Roles: [PAGE_3]"));
    }

    @Test
    public void failDeleteUser() throws IOException {
        Map<String, String> headers = new HashMap<>();
        HttpResponse response = request("users/user1", "DELETE", null, headers);

        assertEquals(401,response.getStatusLine().getStatusCode());
    }


    static HttpResponse request(String path, String method, String body, Map<String, String> headers)  {
        try {

            HttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
            String uri = "http://localhost:8080/" + path;
            HttpUriRequest request;
            switch (method) {
                case "GET":
                    request = new HttpGet(uri);
                    if (null != headers && !headers.isEmpty())
                        for (Map.Entry<String, String> h : headers.entrySet())
                            request.addHeader(h.getKey(), h.getValue());
                    break;
                case "POST":
                    request = new HttpPost(uri);
                    if (null != body)
                        ((HttpPost) request).setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
                    if (null != headers && !headers.isEmpty())
                        for (Map.Entry<String, String> h : headers.entrySet())
                            request.addHeader(h.getKey(), h.getValue());
                    break;
                case "DELETE":
                    request = new HttpDelete(uri);
                    if (null != headers && !headers.isEmpty())
                        for (Map.Entry<String, String> h : headers.entrySet())
                            request.addHeader(h.getKey(), h.getValue());
                    break;
                case "PUT":
                    request = new HttpPut(uri);
                    if (null != body)
                        ((HttpPut) request).setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
                    if (null != headers && !headers.isEmpty())
                        for (Map.Entry<String, String> h : headers.entrySet())
                            request.addHeader(h.getKey(), h.getValue());
                    break;
                default:
                    throw new RuntimeException("Http option not known.");
            }
            return client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
