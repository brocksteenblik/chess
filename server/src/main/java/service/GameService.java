package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import handler.InputException;
import model.*;

import java.util.Collection;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public Collection<ListGamesResult> requestGamesList(ListGamesRequest listGamesRequest){
        AuthData authData = dataAccess.getAuth(listGamesRequest.authToken());
        checkAuthInDB(authData);
        return dataAccess.listGames();
    }

    public CreateGameResult newGame(CreateGameRequest createGameRequest, String authToken){
        AuthData authData = dataAccess.getAuth(authToken);
        checkAuthInDB(authData);
        int gameID = dataAccess.createGame(createGameRequest.gameName());
        return new CreateGameResult(gameID);
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws Unauthorized {
        AuthData authData = dataAccess.getAuth(authToken);
        checkAuthInDB(authData);
        dataAccess.updateGame(authData, joinGameRequest);
    }

    private static void checkAuthInDB(AuthData authData) throws Unauthorized{
        if (authData == null){
            throw new Unauthorized(401, "Error: Unauthorized");
        }
    }
}
