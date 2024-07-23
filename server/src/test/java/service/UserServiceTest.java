package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService;
    UserData user1;
    UserData user2;
    UserData user3;

    @BeforeEach
    void runBefore() {
        userService = new UserService();
        user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        user3 = new UserData("winnie", "bees", "winniethepooh@hawoods.org");
    }

    @Test
    void register() throws DataAccessException {
        AuthData authData1 = userService.register(user1);
        AuthData authData2 = userService.register(user2);
        Assertions.assertEquals("winnie", authData1.getUsername());
        Assertions.assertEquals("eyore", authData2.getUsername());
        Assertions.assertEquals("winnie", userService.getUsers().get("winnie").getUsername());
    }

    @Test
    void registerFail() throws DataAccessException {
        userService.register(user1);
        userService.register(user2);
        AuthData authData3 = userService.register(user3);
        Assertions.assertNull(authData3);
    }

    @Test
    void login() throws DataAccessException {
        userService.register(user1);
        userService.register(user2);
        AuthData authData = userService.login("winnie", "honey");
        Assertions.assertEquals("winnie", authData.getUsername());
    }

    @Test
    void loginFailUsername() throws DataAccessException {
        userService.register(user1);
        userService.register(user2);
        AuthData authData = userService.login("piglet", "honey");
        Assertions.assertNull(authData);
    }
    @Test
    void loginFailPassword() throws DataAccessException {
        userService.register(user1);
        userService.register(user2);
        AuthData authData = userService.login("winnie", "bees");
        Assertions.assertNull(authData);
    }


    @Test
    void logout() throws DataAccessException {
        userService.register(user1);
        userService.register(user2);
        AuthData authData = userService.login("winnie", "honey");
        userService.logout(authData.getAuthToken());
        Assertions.assertNull(userService.getAuths().get(authData.getAuthToken()));
    }

    @Test
    void logoutFail() throws DataAccessException {
        userService.register(user1);
        userService.register(user2);
        AuthData authData = userService.login("winnie", "honey");
        userService.logout("2345");
        Assertions.assertNotEquals(null, userService.getAuths().get(authData.getAuthToken()));
    }
}