package service;

import model.RegisterRequest;
import model.RegisterResult;
import dataaccess.MemoryDataAccess;

public class UserService {

    private final MemoryDataAccess dataAccess;

    public UserService(MemoryDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest){
        isUserDataNew(registerRequest);
        return new RegisterResult("placeholder", "placeholder");
    }

    private void isUserDataNew(RegisterRequest registerRequest) throws AlreadyTaken{
        if (dataAccess.getUser(registerRequest.username()) != null){
            throw new AlreadyTaken(403, "Error: username already taken");
        }
        else if (dataAccess.getUser(registerRequest.email()) != null){
            throw new AlreadyTaken(403, "Error: email already taken");
        }
    }
}
