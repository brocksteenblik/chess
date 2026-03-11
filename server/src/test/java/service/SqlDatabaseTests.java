package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SqlDataAccess;
import model.RegisterRequest;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
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
                Assertions.assertEquals(user, new RegisterRequest(username, password, email));
            }
        }
    }

    @Test
    void negativeTestNewUser() throws SQLException, DataAccessException{
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        try(var conn = DatabaseManager.getConnection()){
            DAO.createUser(new UserData(user.username(), user.password(), user.email()));
            DAO.createUser(new UserData(user.username(), "user.password()", "user.email()"));
            try (var preparedStatement = conn.prepareStatement(
                    """
                        SELECT username, password, email FROM users WHERE username = 'Brock'
                        """)) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                var email = rs.getString("email");
                Assertions.assertEquals(user.email(), email);
            }
        }
    }

    @Test
    void testGetUser() throws SQLException, DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            UserData user = new UserData("Brock", "1234", "email@emails.com");
            DAO.createUser(user);
            Assertions.assertEquals(DAO.getUser("Brock"), user);
        }
    }

    @Test
    void testGetUserNegative() throws SQLException, DataAccessException{
        Assertions.assertNull(DAO.getUser("Brock"));
    }

    @Test
    void testCreateAuth() throws SQLException, DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            DAO.createAuth("", "Brock");
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
