package service;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import handler.InputException;
import model.*;

import java.util.Map;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTaken{
        isUsernameTaken(registerRequest);
        UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        dataAccess.createUser(userData);
        AuthData authData = dataAccess.createAuth("", registerRequest.username());
        return new RegisterResult(authData.username(), authData.authToken());
    }

    public LoginResult login(LoginRequest loginRequest) throws Unauthorized{
        UserData userData = dataAccess.getUser(loginRequest.username());
        authorizeLogin(loginRequest, userData);
        AuthData authData = dataAccess.createAuth("", loginRequest.username());
        return new LoginResult(authData.username(), authData.authToken());
    }

    private static void authorizeLogin(LoginRequest loginRequest, UserData userData) {
        if (userData == null){
            throw new Unauthorized(401, "Error: Unauthorized");
        }
        if (!userData.password().equals(loginRequest.password())){
            throw new Unauthorized(401, "Error: Unauthorized");
        }
    }

    public void logout(LogoutRequest logoutRequest){
        AuthData authData = dataAccess.getAuth(logoutRequest.authToken());
        if (authData == null){
            throw new Unauthorized(401, "Error: Unauthorized");
        }
        dataAccess.deleteAuth(authData);
    }

    public void deleteDB(){
        dataAccess.clear();
    }

    private void isUsernameTaken(RegisterRequest registerRequest) throws AlreadyTaken{
        if (dataAccess.getUser(registerRequest.username()) != null){
            throw new AlreadyTaken(403, "Error: username already taken");
        }
    }
}
