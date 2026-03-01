package service;

import dataaccess.DataAccess;
import model.*;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public ListGamesResult requestGamesList(ListGamesRequest listGamesRequest){
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

    private static void checkAuthInDB(AuthData authData) {
        if (authData == null){
            throw new Unauthorized(401, "Error: Unauthorized");
        }
    }
}
