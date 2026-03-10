package dataaccess;

import model.AuthData;
import model.JoinGameRequest;
import model.ListGamesResult;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlDataAccess implements DataAccess{

    public SqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() {

    }

    @Override
    public void createUser(UserData userData) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public Collection<ListGamesResult> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(AuthData authData, JoinGameRequest joinGameRequest) {

    }

    @Override
    public AuthData createAuth(String authToken, String username) {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) {

    }

    private final String[] createUsers = {
            """
            CREATE TABLE IF NOT EXISTS users (
              'username' varchar(256) NOT NULL,
              'password' varchar(256) NOT NULL,
              'email' varchar(256) NOT NULL,
              PRIMARY KEY ('username')
            )
    """
    };

    private final String[] createAuths = {
            """
            CREATE TABLE IF NOT EXISTS auths (
              'authToken' varchar(256) NOT NULL,
              'username' varchar(256) NOT NULL,
              PRIMARY KEY ('authToken')
            )
    """
    };

    private final String[] createGames = {
            """
            CREATE TABLE IF NOT EXISTS auths (
              'gameID' int NOT NULL,
              'whiteUsername' varchar(256),
              'blackUsername' varchar(256),
              'gameName' varchar(256) NOT NULL,
              'chessGame' varchar(256) NOT NULL,
              PRIMARY KEY ('gameID')
            )
    """
    };

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            createTable(conn, createUsers);
            createTable(conn, createAuths);
            createTable(conn, createGames);
        } catch (SQLException ex) {
            //throw new ResponseException(ResponseException.Code.ServerError, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    private void createTable(Connection conn, String[] tableInfo) throws SQLException {
        for (String statement : tableInfo) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }
    }
}

