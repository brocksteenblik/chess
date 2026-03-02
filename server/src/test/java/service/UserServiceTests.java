package service;

import dataaccess.MemoryDataAccess;
import model.LoginRequest;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTests {
    static final UserService SERVICE = new UserService(new MemoryDataAccess());

    @BeforeEach
    void clear(){
        SERVICE.deleteDB();
    }

    @Test
    void addNewUser(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        Assertions.assertDoesNotThrow(()-> SERVICE.register(user));
    }

    @Test
    void addRepeatUser(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        SERVICE.register(user);
        Assertions.assertThrows(AlreadyTaken.class, ()-> SERVICE.register(user));
    }

    @Test
    void userLogin(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        SERVICE.register(user);
        var login = new LoginRequest("Brock", "1234");
        Assertions.assertDoesNotThrow(()-> SERVICE.login(login));
    }

    @Test
    void wrongUsernameLogin(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        SERVICE.register(user);
        var login = new LoginRequest("Not Brock", "1234");
        Assertions.assertThrows((Unauthorized.class), ()-> SERVICE.login(login));
    }

    @Test
    void wrongPasswordLogin(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        SERVICE.register(user);
        var login = new LoginRequest("Brock", "adsfadsfds");
        Assertions.assertThrows((Unauthorized.class), ()-> SERVICE.login(login));
    }

    @Test
    void logUserOut(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        SERVICE.register(user);
        var login = new LoginRequest("Brock", "1234");
        LoginResult loginResult = SERVICE.login(login);
        LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
        Assertions.assertDoesNotThrow(()-> SERVICE.logout(logoutRequest));
    }

    @Test
    void failToLogout(){
        var user = new RegisterRequest("Brock", "1234", "email@emails.com");
        SERVICE.register(user);
        var login = new LoginRequest("Brock", "1234");
        SERVICE.login(login);
        LogoutRequest logoutRequest = new LogoutRequest("asdfafeaadsfadsdgyadsaf-a");
        Assertions.assertThrows(Unauthorized.class, ()-> SERVICE.logout(logoutRequest));
    }

    @Test
    void clearUsers(){
        var user1 = new RegisterRequest("Brock", "1234", "email@emails.com");
        var user2 = new RegisterRequest("Waddle-D", "1234", "emails@emails.com");
        SERVICE.register(user1);
        SERVICE.register(user2);
        SERVICE.deleteDB();
        Assertions.assertDoesNotThrow(()->{
            SERVICE.register(user1);
            SERVICE.register(user2);}
        );
    }
}
