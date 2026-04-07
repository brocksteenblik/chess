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
        AuthData authData = dataAccess.getAuth(authToken);
        if (authData == null){
            throw new DataAccessException("Error: Invalid Auth Token");
        }
        return authData.username();
    }

    public ChessGame getGame(int gameID) throws DataAccessException {
        GameData gameData = dataAccess.getGame(gameID);
        if (gameData == null){
            throw new DataAccessException("Error: Invalid GameID");
        }
        return gameData.getChessGame();
    }
}
