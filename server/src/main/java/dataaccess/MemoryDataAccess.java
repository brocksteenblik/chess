package dataaccess;

import model.*;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {

    final private HashMap<Integer, RegisterRequest> users = new HashMap<>();

    public RegisterRequest getUser(String username) {
        return users.get(username);
    }
}
