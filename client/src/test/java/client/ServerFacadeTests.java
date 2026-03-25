package client;

import org.junit.jupiter.api.*;
import server.Server;
import client.ServerFacade;
import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private static String url;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        url = "http://localhost:" + port;
        facade = new ServerFacade(url);
        System.out.println("Started test HTTP server on " + port);
    }

    private static void clearDB(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/db"))
                .DELETE()
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void resetForEachTest(){
        clearDB(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void positiveRegisterUser(){
        try {
            var result = facade.userRegistration("Brock", "1234", "email@emails.net");
            RegisterResult expected = new RegisterResult("Brock", "abcdefg");
            Assertions.assertEquals(expected.username(), result.username());
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void negativeRegisterUser(){
        try {
            facade.userRegistration("Brock", "1234", "email@emails.net");
            var result = facade.userRegistration("Brock", "5678", "anotheremail@emails.net");
            Assertions.assertNull(result.username());
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void positiveLogoutUser(){
        try {
            RegisterResult registerResult = facade.userRegistration("Brock", "1234", "email@emails.net");
            Assertions.assertDoesNotThrow(()->facade.userLogout(registerResult.authToken()));
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void negativeLogoutUser(){
        Assertions.assertThrows(ResponseException.class, () -> facade.userLogout(null));
    }

    @Test
    public void positiveLoginUser(){
        try {
            RegisterResult registerResult = facade.userRegistration("Brock", "1234", "email@emails.net");
            facade.userLogout(registerResult.authToken());
            LoginResult loginResult = facade.userLogin("Brock", "1234");
            Assertions.assertEquals(registerResult.username(), loginResult.username());
            Assertions.assertNotEquals(registerResult.authToken(), loginResult.authToken());
        } catch (ResponseException e) {
        }
    }

    @Test
    public void negativeLoginUser(){
        try {
            LoginResult loginResult = facade.userLogin("Brock", "1234");
            Assertions.assertNull(loginResult.username());
            Assertions.assertNull(loginResult.authToken());
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void positiveCreateGame(){
        try{
            RegisterResult registerResult = facade.userRegistration("Brock" , "1234", "email@emails.com");
            Assertions.assertNotNull(facade.userCreateGame("Game-with-a-name", registerResult.authToken()));
        } catch(ResponseException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    public void negativeCreateGame(){
        try {
            Assertions.assertEquals(0, facade.userCreateGame("GameName", null).gameID());
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void positiveListGames(){
        try {
            var registerResult = facade.userRegistration("Brock", "1234", "email@emails.com");
            facade.userCreateGame("Game 1", registerResult.authToken());
            facade.userCreateGame("Game 2", registerResult.authToken());
            var actual = facade.userListGames(registerResult.authToken());
            ArrayList<ListGamesResult> expected = new ArrayList<>();
            if (actual.getFirst().gameName().equals("Game 1")){
                expected.add(new ListGamesResult(actual.get(0).gameID(), null, null, "Game 1"));
                expected.add(new ListGamesResult(actual.get(1).gameID(), null, null, "Game 2"));
            }
            else{
                expected.add(new ListGamesResult(actual.get(0).gameID(), null, null, "Game 2"));
                expected.add(new ListGamesResult(actual.get(1).gameID(), null, null, "Game 1"));
            }
            Assertions.assertEquals(expected, actual);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void negativeListGames(){
        try {
            Assertions.assertNull(facade.userListGames("FAKE AUTH TOKEN"));
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }
}
