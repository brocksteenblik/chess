package dataaccess;

import com.google.gson.Gson;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlDataAccess implements DataAccess{

    public SqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        executeUpdate("TRUNCATE users");
        executeUpdate("TRUNCATE auths");
        executeUpdate("TRUNCATE games");
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException{
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
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

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof UserData p) ps.setString(i + 1, p.toString());
                    else if (param instanceof AuthData p) ps.setString(i + 1, p.toString());
                    else if (param instanceof GameData p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            //throw new ResponseException(ResponseException.Code.ServerError, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
        return 0;
    }

    private final String[] createUsers = {
            """
            CREATE TABLE IF NOT EXISTS users (
              `username` varchar(256) NOT NULL UNIQUE,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
    """
    };

    private final String[] createAuths = {
            """
            CREATE TABLE IF NOT EXISTS auths (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
    """
    };

    private final String[] createGames = {
            """
            CREATE TABLE IF NOT EXISTS games (
              `gameID` int NOT NULL,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `chessGame` varchar(256) NOT NULL,
              PRIMARY KEY (`gameID`)
            )
    """
    };

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            createTable(conn, createUsers);
            createTable(conn, createAuths);
            createTable(conn, createGames);
        } catch (SQLException error) {
            throw new DataAccessException(String.format("Unable to configure database: %s", error));
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

