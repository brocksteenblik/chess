package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Scanner;

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

    public ChessMove askForAndSetPromotion(ChessMove move, ChessPosition start, ChessPosition end) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Select promotion: 'rook', 'knight', 'bishop', 'queen'\n");
            System.out.print(">>> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("rook")) {
                move = new ChessMove(start, end, ChessPiece.PieceType.ROOK);
                break;
            } else if (input.equalsIgnoreCase("knight")) {
                move = new ChessMove(start, end, ChessPiece.PieceType.KNIGHT);
                break;
            } else if (input.equalsIgnoreCase("bishop")) {
                move = new ChessMove(start, end, ChessPiece.PieceType.BISHOP);
                break;
            } else if (input.equalsIgnoreCase("queen")) {
                move = new ChessMove(start, end, ChessPiece.PieceType.QUEEN);
                break;
            } else {
                System.out.print("Invalid piece type. Try again. \n");
            }
        }
        return move;
    }
}
