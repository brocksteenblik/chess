package handler;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import io.javalin.http.Context;
import model.RegisterRequest;
import model.RegisterResult;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import service.UserService;

import java.util.Map;

public class Handler {

    private final UserService userService;

    public Handler(DataAccess memoryDataAccess) {
        this.userService = new UserService(memoryDataAccess);
    }

    public void addUser(@NotNull Context context) throws InputException{
        RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
        // Maybe try doing a Try Catch block?
        if (registerRequest.email() == null
        || registerRequest.password() == null
        || registerRequest.username() == null){
            InputException error = new InputException(400, "Error: bad request");
            context.status(error.getCode());
            context.result(new Gson().toJson(Map.of("message", error.getMessage())));
            return;
        }
        RegisterResult registerResult = userService.register(registerRequest);
        context.result(new Gson().toJson(registerResult));
    }

    public void clear(@NotNull Context context) {
        userService.deleteDB();
    }
}
