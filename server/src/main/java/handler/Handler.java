package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.AlreadyTaken;
import service.GameService;
import service.Unauthorized;
import service.UserService;

import java.sql.SQLException;
import java.util.Map;

public class Handler {

    private final UserService userService;
    private final GameService gameService;

    public Handler(DataAccess memoryDataAccess) {
        this.userService = new UserService(memoryDataAccess);
        this.gameService = new GameService(memoryDataAccess);
    }

    public void addUser(@NotNull Context context) throws InputException {
        RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
        if (registerRequest.email() == null
        || registerRequest.password() == null
        || registerRequest.username() == null){
            giveBadRequest(context);
            return;
        }
        try {
            try{
                RegisterResult registerResult = userService.register(registerRequest);
                context.result(new Gson().toJson(registerResult));
            } catch (DataAccessException | SQLException error){
                context.status(500);
                context.result(new Gson().toJson(Map.of("message", error.getMessage())));
            }
        } catch(AlreadyTaken error){
            context.status(error.getCode());
            context.result(new Gson().toJson(Map.of("message", error.getMessage())));
        }

    }

    public void login(@NotNull Context context) throws Unauthorized, InputException {
        LoginRequest loginRequest = new Gson().fromJson(context.body(), LoginRequest.class);
        if (loginRequest.username() == null
            || loginRequest.password() == null){
            giveBadRequest(context);
            return;
        }
        try {
            LoginResult loginResult = userService.login(loginRequest);
            context.result(new Gson().toJson(loginResult));
        } catch(Unauthorized error){
            context.status(error.getCode());
            context.result(new Gson().toJson(Map.of("message", error.getMessage())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout(@NotNull Context context){
        LogoutRequest logoutRequest = new LogoutRequest(context.header("Authorization"));
        if (logoutRequest.authToken() == null){
            giveBadRequest(context);
        }
        try{
            userService.logout(logoutRequest);
        } catch(Unauthorized error){
            context.status(error.getCode());
            context.result(new Gson().toJson(Map.of("message", error.getMessage())));
        }
    }

    public void getGames(@NotNull Context context) {
        ListGamesRequest listGamesRequest = new ListGamesRequest(context.header("Authorization"));
        try{
            ListGamesResultCollection listGamesResultCollection = new ListGamesResultCollection(gameService.requestGamesList(listGamesRequest));
            context.result(new Gson().toJson(listGamesResultCollection));
        } catch(Unauthorized error){
            context.status(error.getCode());
            context.result(new Gson().toJson(Map.of("message", error.getMessage())));
        }
    }

    public void newGame(@NotNull Context context) throws InputException, Unauthorized{
        CreateGameRequest createGameRequest = new Gson().fromJson(context.body(), CreateGameRequest.class);
        if (createGameRequest.gameName() == null){
            giveBadRequest(context);
            return;
        }
        String authToken = context.header("Authorization");
        try{
            CreateGameResult createGameResult = gameService.newGame(createGameRequest, authToken);
            context.result(new Gson().toJson(createGameResult));
        } catch (Unauthorized error){
            context.status(error.getCode());
            context.result(new Gson().toJson(Map.of("message", error.getMessage())));
        }
    }

    public void newPlayer(@NotNull Context context) throws InputException{
        JoinGameRequest joinGameRequest = new Gson().fromJson(context.body(), JoinGameRequest.class);
        if (joinGameRequest.playerColor() == null
            || joinGameRequest.gameID() == 0){
            giveBadRequest(context);
            return;
        }
        String authToken = context.header("Authorization");
        try{
            try{
                gameService.joinGame(authToken, joinGameRequest);
            } catch (Unauthorized error){
                context.status(error.getCode());
                context.result(new Gson().toJson(Map.of("message", error.getMessage())));
            }
            context.result();
        } catch(InputException error){
            context.status(error.getCode());
            context.result(new Gson().toJson(Map.of("message", error.getMessage())));
        }

    }

    public void clear(@NotNull Context context) throws DataAccessException{
        userService.deleteDB();
    }

    private static void giveBadRequest(@NotNull Context context) throws InputException{
        InputException error = new InputException(400, "Error: bad request");
        context.status(error.getCode());
        context.result(new Gson().toJson(Map.of("message", error.getMessage())));
    }
}
