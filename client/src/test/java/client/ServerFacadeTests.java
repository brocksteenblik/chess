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


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        var url = "http://localhost:" + port;
        facade = new ServerFacade(url);
        clearDB(url);
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

}
