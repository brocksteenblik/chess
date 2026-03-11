package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import handler.InputException;
import model.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlDatabaseTests {
    static final DataAccess DAO;

    static {
        try {
            DAO = new SqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void clearSchema() throws DataAccessException, SQLException {
        DAO.clear();
    }

    @Test
    void testNewUser() throws SQLException, DataAccessException{
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        try(var conn = DatabaseManager.getConnection()){
            DAO.createUser(new UserData(user.username(), user.password(), user.email()));
            try (var preparedStatement = conn.prepareStatement(
                    """
                        SELECT username, password, email FROM users WHERE username = 'Brock'
                        """)) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                var username = rs.getString("username");
                var password = rs.getString("password");
                var email = rs.getString("email");
                Assertions.assertEquals(user.username(), username);
                Assertions.assertEquals(user.email(), email);
                Assertions.assertTrue(BCrypt.checkpw(user.password(), password));
            }
        }
    }

    @Test
    void negativeTestNewUser() throws SQLException, DataAccessException{
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        try(var conn = DatabaseManager.getConnection()){
            DAO.createUser(new UserData(user.username(), user.password(), user.email()));
            Assertions.assertThrows((DataAccessException.class),
                    ()->DAO.createUser(new UserData(user.username(), "user.password()", "user.email()")));

        }
    }

    @Test
    void testGetUser() throws SQLException, DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            UserData user = new UserData("Brock", "1234", "email@emails.com");
            DAO.createUser(user);
            Assertions.assertEquals(DAO.getUser("Brock").username(), user.username());
            Assertions.assertEquals(DAO.getUser("Brock").email(), user.email());
            Assertions.assertTrue(BCrypt.checkpw(user.password(), DAO.getUser("Brock").password()));
        }
    }

    @Test
    void testGetUserNegative() throws SQLException, DataAccessException{
        Assertions.assertNull(DAO.getUser("Brock"));
    }

    @Test
    void testCreateAuth() throws SQLException, DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            AuthData authData = DAO.createAuth("", "Brock");
            try (var preparedStatement = conn.prepareStatement(
                    """
                        SELECT authToken, username FROM auths WHERE username = 'Brock'
                        """)) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                var authToken = rs.getString("authToken");
                var username = rs.getString("username");
                Assertions.assertEquals("Brock", username);
                Assertions.assertNotEquals("", authToken);
            }
        }
    }

    @Test
    void negativeTestCreateAuth() throws SQLException, DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()) {
            DAO.createAuth("", "Brock");
            try (var preparedStatement = conn.prepareStatement(
                    """
                        
                            SELECT authToken, username FROM auths WHERE username = 'Brock'
                        """)) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                var authToken1 = rs.getString("authToken");
                DAO.createAuth("duplicate authToken", "Brock");
                try (var preparedStatement2 = conn.prepareStatement(
                                """
                            
                                SELECT authToken, username FROM auths WHERE username = 'Brock'
                            """)) {
                    var rs2 = preparedStatement2.executeQuery();
                    rs2.next();
                    var authToken2 = rs.getString("authToken");
                    Assertions.assertEquals(authToken1, authToken2);
                }
            }
        }
    }

    @Test
    void testGetAuth() throws SQLException, DataAccessException{
        AuthData authData = DAO.createAuth("", "Brock");
        Assertions.assertEquals(authData, DAO.getAuth(authData.authToken()));
    }

    @Test
    void negativeTestGetAuth() throws SQLException, DataAccessException{
        AuthData authData = DAO.createAuth("", "Brock");
        Assertions.assertNull(DAO.getAuth(""));
    }

    @Test
    void testDeleteAuth() throws DataAccessException{
        AuthData authData = DAO.createAuth("", "Brock");
        DAO.deleteAuth(authData);
        Assertions.assertNull(DAO.getAuth(authData.authToken()));
    }

    @Test
    void negativeTestDeleteAuth() throws DataAccessException{
        AuthData authData = DAO.createAuth("", "Brock");
        DAO.deleteAuth(new AuthData("authToken", "New User"));
        Assertions.assertEquals(authData, DAO.getAuth(authData.authToken()));
    }

    @Test
    void testCreateGame() throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            DAO.createGame("Chess Gaming");
            try (var preparedStatement = conn.prepareStatement(
                    """
                        SELECT gameID, gameName, chessGame FROM games WHERE gameName = 'Chess Gaming'
                        """)) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                var gameID = rs.getString("gameID");
                var gameName = rs.getString("gameName");
                var chessGameJSON = rs.getString("chessGame");
                var chessGame = new Gson().fromJson(chessGameJSON, ChessGame.class);

                Assertions.assertNotNull(gameID);
                Assertions.assertEquals("Chess Gaming", gameName);
                Assertions.assertEquals(new ChessGame(), chessGame);
            }
        } catch (SQLException error) {
        }
    }

    @Test
    void negativeTestCreateGame() throws DataAccessException, SQLException {
        Assertions.assertThrows(DataAccessException.class, ()->DAO.createGame(null));
    }

    @Test
    void testListGames() throws DataAccessException{
        DAO.createGame("Chess Game");
        DAO.createGame("Another Chess Game");
        var gameList = DAO.listGames();
        Assertions.assertEquals(2, gameList.size());
    }

    @Test
    void negativeTestListGames() throws DataAccessException {
        var gameList = DAO.listGames();
        Assertions.assertEquals(0, gameList.size());
    }

    @Test
    void testUpdateGame() throws DataAccessException {
        UserData userData = new UserData("Brock", "1234", "email@emails.com");
        DAO.createUser(userData);
        AuthData authData = DAO.createAuth("", "Brock");
        int gameID = DAO.createGame("Chess Game");
        DAO.updateGame(authData, new JoinGameRequest(JoinGameRequest.PlayerColor.WHITE, gameID));
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(
                    "SELECT whiteUsername FROM games")){
                var rs = preparedStatement.executeQuery();
                rs.next();
                String whiteUsername = rs.getString("whiteUsername");
                Assertions.assertEquals("Brock", whiteUsername);
            }
        }catch (SQLException error){}
    }

    @Test
    void negativeTestUpdateGame() throws DataAccessException{
        UserData userData = new UserData("Brock", "1234", "email@emails.com");
        DAO.createUser(userData);
        UserData userData2 = new UserData("Malicious Agent", "5678", "evilmail@emails.com");
        DAO.createUser(userData2);
        AuthData authData = DAO.createAuth("", "Brock");
        AuthData authData2 = DAO.createAuth("", "Malicious Agent");
        int gameID = DAO.createGame("Chess Game");
        DAO.updateGame(authData, new JoinGameRequest(JoinGameRequest.PlayerColor.WHITE, gameID));
        Assertions.assertThrows(InputException.class, ()->DAO.updateGame(authData2, new JoinGameRequest(JoinGameRequest.PlayerColor.WHITE, gameID)));
    }

    @Test
    void testClearWithUsers() throws SQLException, DataAccessException{
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        try(var conn = DatabaseManager.getConnection()) {
            DAO.createUser(new UserData(user.username(), user.password(), user.email()));
            DAO.createUser(new UserData("user2", "5678", user.email()));
            DAO.clear();
            try (var preparedStatement = conn.prepareStatement(
                    """
                        SELECT * FROM users
                        """)) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                Assertions.assertThrows(SQLException.class, ()->rs.getString("username"));
            }
        }
    }
}
