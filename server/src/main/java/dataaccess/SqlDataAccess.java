package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import handler.InputException;
import model.*;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
        String hashedPassword = hashPassword(userData.password());
        executeUpdate(statement, userData.username(), hashedPassword, userData.email());
    }

    private String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(
                    "SELECT username, password, email FROM users WHERE username = ?")){
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()){
                    if (rs.next()) {
                        return grabUserInfo(username, rs);
                    }
                }catch (SQLException error){
                    throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
                }
            }catch (SQLException error){
                throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
            }
        } catch (SQLException error){
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
        }
        return null;
    }

    @NotNull
    private static UserData grabUserInfo(String username, ResultSet rs) throws DataAccessException{
        try {
            String hashedPassword = rs.getString("password");
            String email = rs.getString("email");
            return new UserData(username, hashedPassword, email);
        } catch (SQLException error) {
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        Random random = new Random();
        int gameID = random.nextInt(1,10000);
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        var statement = "INSERT INTO games (gameID, gameName, chessGame) VALUES (?, ?, ?)";
        Gson serializer = new Gson();
        String gameinfo = serializer.toJson(game.game());
        executeUpdate(statement, game.gameID(), game.gameName(), gameinfo);
        return gameID;
    }

    @Override
    public Collection<ListGamesResult> listGames() throws DataAccessException{
        ArrayList<ListGamesResult> gameList = new ArrayList<>();
        try(Connection conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(
                    "SELECT gameID, whiteUsername, blackUsername, gameName FROM games")){
                    try (ResultSet rs = preparedStatement.executeQuery()) {
                        while (rs.next()) {
                            int gameID = rs.getInt("gameID");
                            String whiteUsername = rs.getString("whiteUsername");
                            String blackUsername = rs.getString("blackUsername");
                            String gameName = rs.getString("gameName");
                            ListGamesResult listGameResult = new ListGamesResult(gameID, whiteUsername, blackUsername, gameName);
                            gameList.add(listGameResult);
                        }
                    }
            }
        } catch (SQLException error){
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
        }
        return gameList;
    }

    @Override
    public void updateGame(AuthData authData, JoinGameRequest joinGameRequest) throws DataAccessException {
        String username = authData.username();
        if (username == null){
            removePlayerFromGame(joinGameRequest);
            return;
        }
        int gameID = joinGameRequest.gameID();
        GameData gameData = getGame(gameID);
        if (joinGameRequest.playerColor() == JoinGameRequest.PlayerColor.WHITE){
            if (gameData.whiteUsername() == null){
                GameData newGameData = gameData.setWhiteUsername(username);
                changeColorUsername(newGameData);
            } else{
                throw new InputException(403, "Error 403: Forbidden");
            }
        } else if (joinGameRequest.playerColor() == JoinGameRequest.PlayerColor.BLACK){
            if (gameData.blackUsername() == null) {
                GameData newGameData = gameData.setBlackUsername(username);
                changeColorUsername(newGameData);
            } else{
                throw new InputException(403, "Error 403: Forbidden");
            }
        }
    }

    private void removePlayerFromGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        int gameID = joinGameRequest.gameID();
        GameData gameData = getGame(gameID);
        if (joinGameRequest.playerColor() == JoinGameRequest.PlayerColor.WHITE){
            if (gameData.whiteUsername() != null){
                GameData newGameData = gameData.setWhiteUsername(null);
                changeColorUsername(newGameData);
            } else {
                throw new InputException(403, "Error 403: Forbidden");
            }
        } else if (joinGameRequest.playerColor() == JoinGameRequest.PlayerColor.BLACK) {
            if (gameData.blackUsername() != null) {
                GameData newGameData = gameData.setBlackUsername(null);
                changeColorUsername(newGameData);
            } else {
                throw new InputException(403, "Error 403: Forbidden");
            }
        }
    }

    private void changeColorUsername(GameData newGameData) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(
                    "UPDATE games SET whiteUsername = ?, blackUsername = ? WHERE gameID = ?")){
                preparedStatement.setString(1, newGameData.whiteUsername());
                preparedStatement.setString(2, newGameData.blackUsername());
                preparedStatement.setInt(3, newGameData.gameID());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException error) {
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
        }
    }


    private GameData grabGameInfo(Integer gameID, ResultSet rs) throws SQLException {
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String chessGameJSON = rs.getString("chessGame");
        ChessGame chessGame = new Gson().fromJson(chessGameJSON, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
    }

    @Override
    public void makeMove(AuthData authData, int gameID, ChessMove move) throws DataAccessException {
        GameData gameData = getGame(gameID);
        ChessGame game = gameData.getChessGame();
        try {
            game.makeMove(move);
            GameData updatedGame = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            changeGameData(updatedGame);
        } catch (InvalidMoveException e) {
            throw new DataAccessException("Error: Invalid Move");
        }
    }

    @Override
    public void endGame(AuthData authData, int gameID) throws DataAccessException {
        GameData gameData = getGame(gameID);
        ChessGame game = gameData.getChessGame();
        game.setGameEnded();
        GameData updatedGame = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        changeGameData(updatedGame);
    }

    private void changeGameData(GameData newGameData) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(
                    "UPDATE games SET chessGame = ? WHERE gameID = ?")){
                preparedStatement.setString(1, new Gson().toJson(newGameData.getChessGame()));
                preparedStatement.setInt(2, newGameData.gameID());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException error) {
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
        }
    }


    @Override
    public AuthData createAuth(String authToken, String username) throws DataAccessException {
        authToken = UUID.randomUUID().toString();
        AuthData authData =  new AuthData(authToken, username);
        var statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, authData.authToken(), authData.username());
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(
                    "SELECT authToken, username FROM auths WHERE authToken = ?")){
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()){
                    if (rs.next()) {
                        return grabAuthInfo(authToken, rs);
                    }
                }catch (SQLException error){
                    throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
                }
            }catch (SQLException error){
                throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
            }
        } catch (SQLException error){
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
        }
        return null;
    }

    private AuthData grabAuthInfo(String authToken, ResultSet rs) throws DataAccessException{
        try {
            String username = rs.getString("username");
            return new AuthData(authToken, username);
        } catch (SQLException error) {
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
        }
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE authToken=?";
        executeUpdate(statement, authData.authToken());
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(
                    "SELECT * FROM games WHERE gameID = ?")) {
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return grabGameInfo(gameID, rs);
                    }
                }
            }
        } catch(SQLException error){
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
        }
        return null;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) {ps.setString(i + 1, p);}
                    else if (param instanceof Integer p) {ps.setInt(i + 1, p);}
                    else if (param instanceof UserData p) {ps.setString(i + 1, p.toString());}
                    else if (param instanceof AuthData p) {ps.setString(i + 1, p.toString());}
                    else if (param instanceof GameData p) {ps.setString(i + 1, p.toString());}
                    else if (param == null) {ps.setNull(i + 1, NULL);}
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException error) {
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
        }
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
              `chessGame` TEXT NOT NULL,
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
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", error));
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

