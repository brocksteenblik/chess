package client;

import chess.ChessGame;
import chess.ChessPiece;
import model.*;

import java.util.ArrayList;

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

    public void userLogout(String authToken) throws ResponseException {
        if (authToken == null){
            throw new ResponseException(401, "Error: Not logged in");
        }
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        communicator.useLogoutEndpoint(serverUrl, logoutRequest);
    }

    public CreateGameResult userCreateGame(String gameName, String authToken) throws ResponseException {
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
        CreateGameResult createGameResult = communicator.useCreateGameEndpoint(serverUrl, createGameRequest, authToken);
        return createGameResult;
    }

    public ArrayList<ListGamesResult> userListGames(String authToken) throws ResponseException{
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ArrayList<ListGamesResult> games = communicator.useListGamesEndpoint(serverUrl, listGamesRequest, authToken);
        return games;
    }

    public void userPlayGame(int gameID, String color, String authToken) throws ResponseException {
        JoinGameRequest joinGameRequest;
        if (color.equals("white")) {
            joinGameRequest = new JoinGameRequest(JoinGameRequest.PlayerColor.WHITE, gameID);
        } else if (color.equals("black")){
            joinGameRequest = new JoinGameRequest(JoinGameRequest.PlayerColor.BLACK, gameID);
        } else {throw new ResponseException(400, "Error: invalid player color");}
        communicator.usePlayGameEndpoint(serverUrl, joinGameRequest, authToken);
    }
}
