package client;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientCommunicator {
    // URL will be http://localhost:8080
    private static final HttpClient client = HttpClient.newHttpClient();

    public RegisterResult useRegisterEndpoint(String url, RegisterRequest registerRequest) throws ResponseException {
        var jsonBody = new Gson().toJson(registerRequest);
        String path = "/user";
        HttpResponse<String> httpResponse = sendNewRequest(url, path, jsonBody);
        // Ask about how to deal with error: name already taken
        RegisterResult registerResult = new Gson().fromJson(httpResponse.body(), RegisterResult.class);
        return registerResult;
    }

    private static HttpResponse<String> sendNewRequest(String url, String path, String body) throws ResponseException {
        String urlWithEndpoint = String.format(url + path);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithEndpoint))
                .timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try{
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception error){
            throw new ResponseException(500, error.getMessage());
        }
    }
}
