package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ClientCommunicator {
    // URL will be http://localhost:8080
    private static final HttpClient client = HttpClient.newHttpClient();

    public RegisterResult useRegisterEndpoint(String url, RegisterRequest registerRequest) throws ResponseException {
        var jsonBody = new Gson().toJson(registerRequest);
        String path = "/user";
        HttpResponse<String> httpResponse = sendNewRequest("POST", url, path, jsonBody, null);
        // Ask about how to deal with error: name already taken
        RegisterResult registerResult = new Gson().fromJson(httpResponse.body(), RegisterResult.class);
        return registerResult;
    }

    public LoginResult useLoginEndpoint(String url, LoginRequest loginRequest) throws ResponseException{
        var jsonBody = new Gson().toJson(loginRequest);
        String path = "/session";
        HttpResponse<String> httpResponse = sendNewRequest("POST", url, path, jsonBody, null);
        LoginResult loginResult = new Gson().fromJson(httpResponse.body(), LoginResult.class);
        return loginResult;
    }

    public void useLogoutEndpoint(String url, LogoutRequest logoutRequest) throws ResponseException{
        var jsonBody = new Gson().toJson(logoutRequest);
        String path = "/session";
        sendNewRequest("DELETE", url, path, jsonBody, logoutRequest.authToken());
    }

    public CreateGameResult useCreateGameEndpoint(String url, CreateGameRequest createGameRequest, String header) throws ResponseException{
        var jsonBody = new Gson().toJson(createGameRequest);
        String path = "/game";
        HttpResponse<String> httpResponse = sendNewRequest("POST", url, path, jsonBody, header);
        CreateGameResult createGameResult = new Gson().fromJson(httpResponse.body(), CreateGameResult.class);
        return createGameResult;
    }

    public ArrayList<ListGamesResult> useListGamesEndpoint(String url, ListGamesRequest listGamesRequest, String header) throws ResponseException {
        var jsonBody = new Gson().toJson(listGamesRequest);
        String path = "/game";
        HttpResponse<String> httpResponse = sendNewRequest("GET", url, path, jsonBody, header);
        ArrayList<ListGamesResult> games = new Gson().fromJson(httpResponse.body(), ListGamesResultCollection.class).games();
        return games;
    }

    private static HttpResponse<String> sendNewRequest(String method, String url, String path, String body, String header) throws ResponseException {
        String urlWithEndpoint = String.format(url + path);
        HttpRequest request = createNewRequest(method, body, header, urlWithEndpoint);
        try{
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception error){
            throw new ResponseException(500, error.getMessage());
        }
    }

    private static HttpRequest createNewRequest(String method, String body, String header, String urlWithEndpoint) {
        if (header != null) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlWithEndpoint))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .method(method, HttpRequest.BodyPublishers.ofString(body))
                    .header("Authorization", header)
                    .build();
            return request;
        }
        else {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlWithEndpoint))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .method(method, HttpRequest.BodyPublishers.ofString(body))
                    .build();
            return request;
        }
    }
}
