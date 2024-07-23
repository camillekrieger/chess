package service;

import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void listGames() {
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
        Assertions.assertTrue(gameService.ListGames().isEmpty());
    }
}