package jp.kobe_u.cspiral.cloudgarden.devices;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;

public class RestClient {
    private String account = null;
    private String password = null;

    public RestClient(String account, String password) {
        this.account = account;
        this.password = password;
    }

    private Client getClient() {
        Client client = new Client();
        return client;
    }

    /**
     * RESTful Webサービスに対してPOST or PUTメソッドを送信する.
     */
    private <E> String sendRequest(String uri, E entity, String method,
            Class<E> cls, MediaType type) {

        Client client = getClient();
        ClientRequest.Builder builder = ClientRequest.create();
        try {
            builder.type(type).entity(entity);
            ClientRequest request = builder.build(new URI(uri), method);
            ClientResponse response = client.handle(request);
            switch (response.getStatus()) {
            case 200:   // OK
            case 201:   // CREATED
                return response.getEntity(String.class);
            default:    // OK, CREATED 以外
                String error = response.getEntity(String.class);
                throw new RuntimeException(error);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * RESTful Webサービスに対してPOSTメソッドを送信する.
     */
    public <E> String post(String uri, E entity, Class<E> cls, MediaType type) {
        return sendRequest(uri, entity, HttpMethod.POST, cls, type);
    }
}

