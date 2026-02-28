package server;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import handler.Handler;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.UserService;

public class Server {

    private final Javalin javalin;


    public Server() {
        DataAccess memoryDataAccess = new MemoryDataAccess();
        Handler handler = new Handler(memoryDataAccess);
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", handler::addUser)
                .post("/session", handler::login)
                .delete("/session", handler::logout)
                .get("/game", handler::getGames)
                .post("/game", handler::newGame)
                .put("/game", this::newPlayer)
                .delete("/db", handler::clear)
        ;


    }

    private void newPlayer(@NotNull Context context) {

    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
