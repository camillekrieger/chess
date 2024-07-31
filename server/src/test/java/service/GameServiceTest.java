package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
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
    void before() throws DataAccessException, SQLException {
        gameService = new GameService();
        userService = new UserService();
        user1 = new UserData("winnie", "honey", "wtp@hawoods.org");
        user2 = new UserData("eyore", "tailgone", "edonkey@hawoods.org");
        auth1 = userService.register(user1);
        auth2 = userService.register(user2);
    }

    @Test
    void listGames() throws DataAccessException, SQLException {
        int gameID = gameService.createGame(auth1.getAuthToken(), "hundred acre tournament");
        Collection<GameData> list = gameService.listGames(auth1.getAuthToken());
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void listGamesFail() throws DataAccessException, SQLException {
        int gameID = gameService.createGame(auth1.getAuthToken(), "hundred acre tournament");
        Collection<GameData> list = gameService.listGames("1234");
        Assertions.assertNull(list);
    }

    @Test
    void createGame() throws DataAccessException, SQLException {
        UserData user = new UserData("pooh bear", "christopher", "honeyisgood@hawoods.org");
        AuthData auth = userService.register(user);
        int gameID = gameService.createGame(auth.getAuthToken(), "tournament");
        int result = gameService.getPreviousGameID();
        Assertions.assertEquals(result, gameID);
    }

    @Test
    void createGameFailAuth() throws DataAccessException, SQLException {
        int gameID = gameService.createGame("1234", "hundred acre tournament");
        Assertions.assertEquals(0, gameID);
    }

    @Test
    void createGameFailGameAlreadyInPlay() throws DataAccessException, SQLException {
        gameService.createGame(auth1.getAuthToken(), "hundred acre tournament");
        int gameID = gameService.createGame(auth2.getAuthToken(), "hundred acre tournament");
        Assertions.assertEquals(-1, gameID);
    }

    @Test
    void joinGame() throws DataAccessException, SQLException {
        UserData user = new UserData("pooh bear", "christopher", "honeyisgood@hawoods.org");
        AuthData auth = userService.register(user);
        int gameID = gameService.createGame(auth.getAuthToken(), "hundred acre tournament");
        String result = gameService.joinGame(auth.getAuthToken(), ChessGame.TeamColor.WHITE, gameID);
        Assertions.assertEquals("{}", result);
    }

    @Test
    void joinGameFail() throws DataAccessException, SQLException {
        UserData user = new UserData("pooh bear", "christopher", "honeyisgood@hawoods.org");
        AuthData auth = userService.register(user);
        UserData userNext = new UserData("piglet", "balloon", "pig@hawoods.org");
        AuthData authNext = userService.register(userNext);
        int gameID = gameService.createGame(auth.getAuthToken(), "tournament");
        gameService.joinGame(auth.getAuthToken(), ChessGame.TeamColor.WHITE, gameID);
        String result = gameService.joinGame(null, ChessGame.TeamColor.BLACK, gameID);
        Assertions.assertNull(result);
    }
}