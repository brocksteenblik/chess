package server;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SqlDataAccess;
import handler.Handler;
import handler.websocket.WebSocketHandler;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.UserService;

public class Server {

    private final Javalin javalin;


    public Server() {
        try{
            DataAccess memoryDataAccess = new SqlDataAccess();
            Handler handler = new Handler(memoryDataAccess);
            WebSocketHandler webSocketHandler = new WebSocketHandler();
            javalin = Javalin.create(config -> config.staticFiles.add("web"))
                    .post("/user", handler::addUser)
                    .post("/session", handler::login)
                    .delete("/session", handler::logout)
                    .get("/game", handler::getGames)
                    .post("/game", handler::newGame)
                    .put("/game", handler::newPlayer)
                    .delete("/db", handler::clear)
                    .ws("/ws", ws ->{
                        ws.onConnect(webSocketHandler);
                        ws.onMessage(webSocketHandler);
                        ws.onClose(webSocketHandler);
                    });
        }
        catch (DataAccessException error){
            throw new RuntimeException(error);
        }

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
