package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import handler.websocket.ConnectionManager;
import model.AuthData;
import model.GameData;
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

    public ChessGame getGame(int gameID) throws DataAccessException {
        checkValidGameID(gameID);
        GameData gameData = dataAccess.getGame(gameID);
        return gameData.getChessGame();
    }

    public void checkValidGameID(int gameID) throws DataAccessException{
        if (dataAccess.getGame(gameID) == null){
            throw new DataAccessException("Error: Invalid GameID");
        }
    }
}
