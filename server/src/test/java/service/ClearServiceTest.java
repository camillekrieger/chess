package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    @Test
    void clear() throws DataAccessException, SQLException {
        UserService userService = new UserService();
        GameService gameService = new GameService();
        ClearService clearService = new ClearService();
        clearService.clear();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        AuthData authData1 = userService.register(user1);
        AuthData authData2 = userService.register(user2);
        gameService.createGame(authData1.getAuthToken(), "hundred acre tournament");
        clearService.clear();
        Assertions.assertTrue(userService.getUsers().isEmpty());
        Assertions.assertTrue(userService.getAuths().isEmpty());
        Assertions.assertTrue(gameService.getGames().isEmpty());
    }
}