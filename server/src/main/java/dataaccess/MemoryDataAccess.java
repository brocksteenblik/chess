package dataaccess;

import model.*;
import java.util.HashMap;
import java.util.UUID;

public class MemoryDataAccess implements DataAccess {

    final private HashMap<String, UserData> users = new HashMap<>();

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void createUser(UserData userData){
        users.put(userData.username(), userData);
    }

    public AuthData createAuth(String authToken, String username){
        authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, username);
    }
}
