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

    public RegisterResult register(RegisterRequest registerRequest){
        isUsernameTaken(registerRequest);
        UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        dataAccess.createUser(userData);
        AuthData authData = dataAccess.createAuth("", registerRequest.username());
        return new RegisterResult(authData.username(), authData.authToken());
    }

    public LoginResult login(LoginRequest loginRequest){
        UserData userData = dataAccess.getUser(loginRequest.username());
        if (userData == null){
            // Implement InvalidUsernameException later
        }
        if (!userData.password().equals(loginRequest.password())){
            //Implement InvalidPasswordException later
        }
        AuthData authData = dataAccess.createAuth("", loginRequest.username());
        return new LoginResult(authData.username(), authData.authToken());
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
