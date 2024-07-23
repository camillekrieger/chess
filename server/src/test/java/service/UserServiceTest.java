package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void register() throws DataAccessException {
        UserService userService = new UserService();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        AuthData authData1 = userService.register(user1);
        AuthData authData2 = userService.register(user2);
        Assertions.assertEquals("winnie", authData1.getUsername());
        Assertions.assertEquals("eyore", authData2.getUsername());
        Assertions.assertEquals("winnie", userService.getUsers().get("winnie").getUsername());
    }

    @Test
    void registerFail() throws DataAccessException {
        UserService userService = new UserService();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        UserData user3 = new UserData("winnie", "bees", "winniethepooh@hawoods.org");
        userService.register(user1);
        userService.register(user2);
        AuthData authData3 = userService.register(user3);
        Assertions.assertNull(authData3);
    }

    @Test
    void login() throws DataAccessException {
        UserService userService = new UserService();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        userService.register(user1);
        userService.register(user2);
        AuthData authData = userService.login("winnie", "honey");
        Assertions.assertEquals("winnie", authData.getUsername());
    }

    @Test
    void loginFailUsername() throws DataAccessException {
        UserService userService = new UserService();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        userService.register(user1);
        userService.register(user2);
        AuthData authData = userService.login("piglet", "honey");
        Assertions.assertNull(authData);
    }
    @Test
    void loginFailPassword() throws DataAccessException {
        UserService userService = new UserService();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        userService.register(user1);
        userService.register(user2);
        AuthData authData = userService.login("winnie", "bees");
        Assertions.assertNull(authData);
    }


    @Test
    void logout() throws DataAccessException {
        UserService userService = new UserService();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        userService.register(user1);
        userService.register(user2);
        AuthData authData = userService.login("winnie", "honey");
        userService.logout(authData.getAuthToken());
        Assertions.assertNull(userService.getAuths().get(authData.getAuthToken()));
    }

    @Test
    void logoutFail() throws DataAccessException {
        UserService userService = new UserService();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        userService.register(user1);
        userService.register(user2);
        AuthData authData = userService.login("winnie", "honey");
        userService.logout("2345");
        Assertions.assertNotEquals(null, userService.getAuths().get(authData.getAuthToken()));
    }

    @Test
    void clear() throws DataAccessException {
        UserService userService = new UserService();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        userService.register(user1);
        userService.register(user2);
        userService.clear();
        Assertions.assertTrue(userService.getUsers().isEmpty());
        Assertions.assertTrue(userService.getAuths().isEmpty());
    }
}