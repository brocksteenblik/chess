package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.SqlDataAccess;
import handler.websocket.ConnectionManager;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;
import org.eclipse.jetty.websocket.api.Session;

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
}
