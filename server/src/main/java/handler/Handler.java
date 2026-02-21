package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.UserData;
import org.jetbrains.annotations.NotNull;

public class Handler {

    public void addUser(@NotNull Context context) throws InputException{
        UserData userData = new Gson().fromJson(context.body(), UserData.class);
        if (userData.email() == null
        || userData.password() == null
        || userData.username() == null){
            throw new InputException(400, "Error: bad request");
        }

    }
}
