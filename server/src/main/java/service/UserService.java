package service;

import dataaccess.DataAccess;
import model.AuthData;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest){
        isUsernameTaken(registerRequest);
        UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        dataAccess.createUser(userData);
        AuthData authData = new AuthData("", registerRequest.username());
        return new RegisterResult("placeholder", "placeholder");
    }

    private void isUsernameTaken(RegisterRequest registerRequest) throws AlreadyTaken{
        if (dataAccess.getUser(registerRequest.username()) != null){
            throw new AlreadyTaken(403, "Error: username already taken");
        }
    }
}
