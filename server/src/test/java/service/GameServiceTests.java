package service;


import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import handler.InputException;
import model.CreateGameRequest;
import model.LoginRequest;
import model.RegisterRequest;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameServiceTests {
    static MemoryDataAccess memory = new MemoryDataAccess();
    static final UserService USER_SERVICE = new UserService(memory);
    static final GameService GAME_SERVICE = new GameService(memory);

    @BeforeEach
    void clear() throws DataAccessException{
        GAME_SERVICE.deleteDB();
    }

    @Test
    void createNewGame(){
        var newUser = new RegisterRequest("Brock", "1234", "email@emails.com");
        var createGameRequest = new CreateGameRequest("Named Game");
        Assertions.assertDoesNotThrow(() -> GAME_SERVICE.newGame(createGameRequest, USER_SERVICE.register(newUser).authToken()));
    }

    @Test
    void createNewGameWithBadAuthToken(){
        var newUser = new RegisterRequest("Brock", "1234", "email@emails.com");
        var createGameRequest = new CreateGameRequest("Named Game");
        Assertions.assertThrows((Unauthorized.class), () -> GAME_SERVICE.newGame(createGameRequest, "Bad Auth Token"));
    }

    @Test
    void listAllGames() throws SQLException {
        List<ListGamesResult> expected = new ArrayList<>();
        var newUser = new RegisterRequest("Brock", "1234", "email@emails.com");
        try{
            var registeredUser = USER_SERVICE.register(newUser);
            CreateGameResult createResult1 = GAME_SERVICE.newGame(new CreateGameRequest("First Game"), registeredUser.authToken());
            CreateGameResult createResult2 = GAME_SERVICE.newGame(new CreateGameRequest("Second Game"), registeredUser.authToken());
            ArrayList<ListGamesResult> actual = (ArrayList<ListGamesResult>) GAME_SERVICE.requestGamesList(
                    new ListGamesRequest(registeredUser.authToken()));
            if (actual.getFirst().gameName().equals("First Game")){
                expected.add(new ListGamesResult(createResult1.gameID(), null, null, "First Game"));
                expected.add(new ListGamesResult(createResult2.gameID(), null, null, "Second Game"));
            }
            else{
                expected.add(new ListGamesResult(createResult2.gameID(), null, null, "Second Game"));
                expected.add(new ListGamesResult(createResult1.gameID(), null, null, "First Game"));
            }
            Assertions.assertEquals(expected, GAME_SERVICE.requestGamesList(new ListGamesRequest(registeredUser.authToken())));
        } catch(DataAccessException error){

        }

    }

    @Test
    void listGamesWithoutRegistration(){
        Assertions.assertThrows(Unauthorized.class, ()->GAME_SERVICE.requestGamesList(new ListGamesRequest("Bad Auth Token")));
    }

    @Test
    void joinGame() throws SQLException{
        List<ListGamesResult> expected = new ArrayList<>();
        var newUser = new RegisterRequest("Brock", "1234", "email@emails.com");
        try{
            var registeredUser = USER_SERVICE.register(newUser);
            CreateGameResult createResult1 = GAME_SERVICE.newGame(new CreateGameRequest("First Game"), registeredUser.authToken());
            JoinGameRequest joinGameRequest = new JoinGameRequest(JoinGameRequest.PlayerColor.WHITE, createResult1.gameID());
            GAME_SERVICE.joinGame(registeredUser.authToken(), joinGameRequest);
            expected.add(new ListGamesResult(createResult1.gameID(), "Brock", null, "First Game"));
            Assertions.assertEquals(expected, GAME_SERVICE.requestGamesList(new ListGamesRequest(registeredUser.authToken())));

        } catch(DataAccessException error){
        }
    }

    @Test
    void tryToStealColor() throws SQLException{
        List<ListGamesResult> expected = new ArrayList<>();
        var newUser = new RegisterRequest("Brock", "1234", "email@emails.com");
        var newUser2 = new RegisterRequest("Schemer Man", "5678", "emailaccount@emails.com");
        try{
            var registeredUser = USER_SERVICE.register(newUser);
            var registeredUser2 = USER_SERVICE.register(newUser2);
            CreateGameResult createResult1 = GAME_SERVICE.newGame(new CreateGameRequest("First Game"), registeredUser.authToken());
            JoinGameRequest joinGameRequest = new JoinGameRequest(JoinGameRequest.PlayerColor.WHITE, createResult1.gameID());
            JoinGameRequest joinGameRequest2 = new JoinGameRequest(JoinGameRequest.PlayerColor.WHITE, createResult1.gameID());
            GAME_SERVICE.joinGame(registeredUser.authToken(), joinGameRequest);
            Assertions.assertThrows(InputException.class, ()->GAME_SERVICE.joinGame(registeredUser2.authToken(), joinGameRequest2));
        } catch(DataAccessException error){
        }
    }
}
