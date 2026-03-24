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
        // Should I add something that checks if the server's running or not?
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult registerResult = communicator.useRegisterEndpoint(serverUrl, registerRequest);
        return registerResult;
    }

    public LoginResult userLogin(String username, String password) throws ResponseException{
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = communicator.useLoginEndpoint(serverUrl, loginRequest);
        return loginResult;
    }

}
