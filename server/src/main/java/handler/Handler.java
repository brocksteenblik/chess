package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import io.javalin.http.Context;
import model.*;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import service.AlreadyTaken;
import service.UserService;

import java.util.Map;

public class Handler {

    private final UserService userService;

    public Handler(DataAccess memoryDataAccess) {
        this.userService = new UserService(memoryDataAccess);
    }

    public void addUser(@NotNull Context context) throws InputException{
        RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
        if (registerRequest.email() == null
        || registerRequest.password() == null
        || registerRequest.username() == null){
            giveBadRequest(context);
            return;
        }
        try {
            RegisterResult registerResult = userService.register(registerRequest);
            context.result(new Gson().toJson(registerResult));
        } catch(AlreadyTaken error){
            context.status(error.getCode());
            context.result(new Gson().toJson(Map.of("message", error.getMessage())));
        }

    }

    public void login(@NotNull Context context){
        LoginRequest loginRequest = new Gson().fromJson(context.body(), LoginRequest.class);
        if (loginRequest.username() == null
            || loginRequest.password() == null){
            giveBadRequest(context);
        }
        LoginResult loginResult = userService.login(loginRequest);
        context.result(new Gson().toJson(loginResult));
    }

    public void clear(@NotNull Context context) {
        userService.deleteDB();
    }

    private static void giveBadRequest(@NotNull Context context) {
        InputException error = new InputException(400, "Error: bad request");
        context.status(error.getCode());
        context.result(new Gson().toJson(Map.of("message", error.getMessage())));
    }
}
