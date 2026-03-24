package client;

import model.*;

import java.net.http.HttpClient;

public class ServerFacade {
    private final String serverUrl;
    private final ClientCommunicator communicator;

    public ServerFacade(String url){
        serverUrl = url;
        communicator = new ClientCommunicator();
    }

    public RegisterResult userRegistration(String username, String password, String email) throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult registerResult = communicator.useRegisterEndpoint(serverUrl, registerRequest);
        return registerResult;
    }
}
