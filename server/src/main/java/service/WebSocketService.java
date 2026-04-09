package service;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.SqlDataAccess;
import handler.websocket.ConnectionManager;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;

public class WebSocketService {
    private final DataAccess dataAccess;

    public WebSocketService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public String getUsernameFromAuth(String authToken) throws DataAccessException {
        checkValidAuth(authToken);
        AuthData authData = dataAccess.getAuth(authToken);
        return authData.username();
    }

    public void checkValidAuth(String authToken) throws DataAccessException{
        if (dataAccess.getAuth(authToken) == null){
            throw new DataAccessException("Error: Invalid Auth Token");
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        checkValidGameID(gameID);
        return dataAccess.getGame(gameID);
    }

    public void checkValidGameID(int gameID) throws DataAccessException{
        if (dataAccess.getGame(gameID) == null){
            throw new DataAccessException("Error: Invalid GameID");
        }
    }

    public boolean checkIfPlayer(String username, int gameID) throws DataAccessException {
        checkValidGameID(gameID);
        GameData gameData = dataAccess.getGame(gameID);
        return gameData.whiteUsername().equals(username) || gameData.blackUsername().equals(username);
    }

    public void removePlayerFromGame(String authToken, int gameID) throws DataAccessException {
        AuthData authData = dataAccess.getAuth(authToken);
        GameData gameData = dataAccess.getGame(gameID);
        JoinGameRequest joinGameRequest;
        if (gameData.whiteUsername().equals(authData.username())){
            joinGameRequest = new JoinGameRequest(JoinGameRequest.PlayerColor.WHITE, gameID);
        }
        else {
            joinGameRequest = new JoinGameRequest(JoinGameRequest.PlayerColor.BLACK, gameID);
        }
        dataAccess.updateGame(new AuthData(authToken, null), joinGameRequest);
    }

    public boolean checkValidMove(String authToken, int gameID, ChessMove move) throws DataAccessException {
        String username = getUsernameFromAuth(authToken);
        GameData game = getGame(gameID);
        matchUserToColor(username, game, move);
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) game.getChessGame().validMoves(move.getStartPosition());
        for (ChessMove m : validMoves){
            if (m.equals(move)){
                return true;
            }
        }
        return false;
    }

    private void matchUserToColor(String username, GameData game, ChessMove move) throws DataAccessException {
        ChessBoard board = game.getChessGame().getBoard();
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null){
            throw new DataAccessException("Error: Invalid starting position");
        }
        ChessGame.TeamColor color = piece.getTeamColor();
        if (color.equals(ChessGame.TeamColor.WHITE)){
            if (!username.equals(game.whiteUsername())){
                throw new DataAccessException("Error: Invalid Team Color");
            }
        }
        else if (color.equals(ChessGame.TeamColor.BLACK)){
            if (!username.equals(game.blackUsername())){
                throw new DataAccessException("Error: Invalid Team Color");
            }
        }
    }

    public void executeMove(AuthData authData, int gameID, ChessMove move) throws DataAccessException {
        dataAccess.makeMove(authData, gameID, move);
    }
}
