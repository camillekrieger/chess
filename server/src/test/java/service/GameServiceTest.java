package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    GameService gameService;
    UserService userService;
    UserData user1;
    UserData user2;
    AuthData auth1;
    AuthData auth2;
    @BeforeEach
    void before() throws DataAccessException {
        gameService = new GameService();
        userService = new UserService();
        user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        auth1 = userService.register(user1);
        auth2 = userService.register(user2);
    }

    @Test
    void listGames() throws DataAccessException {
        Collection<GameData> gamesTest = new ArrayList<>();
        int gameID = gameService.createGame(auth1.getAuthToken(), "hundred acre tournament");
        gamesTest.add(gameService.getGames().get(gameID));
        Collection<GameData> list = gameService.ListGames(auth1.getAuthToken());
        Assertions.assertEquals(gamesTest.toString(), list.toString());
    }

    @Test
    void listGamesFail() throws DataAccessException {
        int gameID = gameService.createGame(auth1.getAuthToken(), "hundred acre tournament");
        Collection<GameData> list = gameService.ListGames("1234");
        Assertions.assertNull(list);
    }

    @Test
    void createGame() throws DataAccessException {
        int gameID = gameService.createGame(auth1.getAuthToken(), "hundred acre tournament");
        Assertions.assertEquals(gameService.getByName("hundred acre tournament").getGameID(), gameID);
    }

    @Test
    void createGameFailAuth() throws DataAccessException {
        int gameID = gameService.createGame("1234", "hundred acre tournament");
        Assertions.assertEquals(0, gameID);
    }

    @Test
    void createGameFailGameAlreadyInPlay() throws DataAccessException {
        gameService.createGame(auth1.getAuthToken(), "hundred acre tournament");
        int gameID = gameService.createGame(auth2.getAuthToken(), "hundred acre tournament");
        Assertions.assertEquals(0, gameID);
    }

    @Test
    void joinGame() throws DataAccessException {
        int gameID = gameService.createGame(auth1.getAuthToken(), "hundred acre tournament");
        gameService.joinGame(auth2.getAuthToken(), ChessGame.TeamColor.BLACK, gameID);
        Assertions.assertEquals("eyore", gameService.getGames().get(gameID).getBlackUsername());
    }

    @Test
    void joinGameFail() throws DataAccessException {
        int gameID = gameService.createGame(auth1.getAuthToken(), "hundred acre tournament");
        gameService.joinGame(auth2.getAuthToken(), ChessGame.TeamColor.WHITE, gameID);
        assertNull(gameService.getGames().get(gameID).getBlackUsername());
    }
}