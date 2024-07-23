package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void listGames() throws DataAccessException {
        Collection<GameData> gamesTest = new ArrayList<>();
        GameService gameService = new GameService();
        UserService userService = new UserService();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        AuthData auth1 = userService.register(user1);
        AuthData auth2 = userService.register(user2);
        int gameID = gameService.createGame(user1, user2, "hundred acre tournament");
        gamesTest.add(gameService.getGames().get(gameID));
        Collection<GameData> list = gameService.ListGames(auth1.getAuthToken());
        Assertions.assertEquals(gamesTest.toString(), list.toString());
    }

    @Test
    void listGamesFail() throws DataAccessException {
        Collection<GameData> gamesTest = new ArrayList<>();
        GameService gameService = new GameService();
        UserService userService = new UserService();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        AuthData auth1 = userService.register(user1);
        AuthData auth2 = userService.register(user2);
        int gameID = gameService.createGame(user1, user2, "hundred acre tournament");
        gamesTest.add(gameService.getGames().get(gameID));
        Collection<GameData> list = gameService.ListGames("1234");
        Assertions.assertNull(list);
    }

    @Test
    void createGame() {
    }

    @Test
    void joinGame() {
    }

    @Test
    void clear() throws DataAccessException {
        GameService gameService = new GameService();
        UserData user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        UserData user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        gameService.createGame(user1, user2, "hundred acre tourny");
        gameService.clear();
//        Assertions.assertTrue(gameService.ListGames().isEmpty());
    }
}