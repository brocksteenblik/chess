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
        if (authData == null){
            throw new Unauthorized(401, "Error: Unauthorized");
        }
        return dataAccess.listGames();
    }
}
