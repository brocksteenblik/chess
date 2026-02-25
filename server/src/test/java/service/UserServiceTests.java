package service;

import dataaccess.MemoryDataAccess;
import model.LoginRequest;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTests {
    static final UserService service = new UserService(new MemoryDataAccess());

    @BeforeEach
    void clear(){
        service.deleteDB();
    }

    @Test
    void addNewUser(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        Assertions.assertDoesNotThrow(()->service.register(user));
    }

    @Test
    void addRepeatUser(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        service.register(user);
        Assertions.assertThrows(AlreadyTaken.class, ()->service.register(user));
    }

    @Test
    void userLogin(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        service.register(user);
        var login = new LoginRequest("Brock", "1234");
        Assertions.assertDoesNotThrow(()->service.login(login));
    }

    @Test
    void wrongUsernameLogin(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        service.register(user);
        var login = new LoginRequest("Not Brock", "1234");
        Assertions.assertThrows((Unauthorized.class), ()->service.login(login));
    }

    @Test
    void wrongPasswordLogin(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        service.register(user);
        var login = new LoginRequest("Brock", "adsfadsfds");
        Assertions.assertThrows((Unauthorized.class), ()->service.login(login));
    }

    @Test
    void clearUsers(){
        var user1 = new RegisterRequest("Brock", "1234", "email@emails.com");
        var user2 = new RegisterRequest("Waddle-D", "1234", "emails@emails.com");
        service.register(user1);
        service.register(user2);
        service.deleteDB();
        Assertions.assertDoesNotThrow(()->{service.register(user1);
                                           service.register(user2);}
        );
    }
}
