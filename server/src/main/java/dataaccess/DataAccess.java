package dataaccess;

import model.*;

import java.sql.SQLException;
import java.util.Collection;

public interface DataAccess {
    //clear: A method for clearing all data from the database. This is used during testing.
    void clear() throws DataAccessException;

    //createUser: Create a new user.
    void createUser(UserData userData) throws DataAccessException;

    //getUser: Retrieve a user with the given username.
    UserData getUser(String username) throws SQLException, DataAccessException;

    //createGame: Create a new game.
    int createGame(String gameName);

    //getGame: Retrieve a specified game with the given game ID.

    //listGames: Retrieve all games.
    Collection<ListGamesResult> listGames();

    //updateGame: Updates a chess game.
    void updateGame(AuthData authData, JoinGameRequest joinGameRequest);

    //createAuth: Create a new authorization.
    AuthData createAuth(String authToken, String username) throws DataAccessException;

    //getAuth: Retrieve an authorization given an authToken.
    AuthData getAuth(String authToken);

    //deleteAuth: Delete an authorization so that it is no longer valid
    void deleteAuth(AuthData authData);
}
