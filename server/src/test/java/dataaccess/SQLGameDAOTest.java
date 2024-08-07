package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

class SQLGameDAOTest {

    SQLGameDAO sgd;

    @BeforeEach
    void runBefore() throws SQLException, DataAccessException {
        sgd = new SQLGameDAO();
        sgd.clear();
    }

    @Test
    void createGame() throws SQLException, DataAccessException {
        int id = sgd.createGame("hundred acre woods", "winnie", null);
        Assertions.assertNotNull(sgd.getGame(id));
    }

    @Test
    void getGame() throws SQLException, DataAccessException {
        int id = sgd.createGame("hundred acre woods", "winnie", null);
        GameData data = sgd.getGame(id);
        Assertions.assertEquals("winnie", data.getWhiteUsername());
        Assertions.assertNull(data.getBlackUsername());
        Assertions.assertEquals("hundred acre woods", data.getGameName());
    }

    @Test
    void getGameFail() throws SQLException, DataAccessException {
        sgd.createGame("hundred acre woods", "winnie", null);
        Assertions.assertNull(sgd.getGame(0));
    }

    @Test
    void listGames() throws SQLException, DataAccessException {
        sgd.createGame("hundred acre woods", "winnie", null);
        Collection<GameData> list = sgd.listGames();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void updateGame() throws SQLException, DataAccessException {
        sgd.createGame("hundred acre woods", null, null);
        GameData data = sgd.getGameByName("hundred acre woods");
        String result = sgd.updateGame(data, ChessGame.TeamColor.WHITE, "winnie");
        Assertions.assertEquals("{}", result);
        String blackResult = sgd.updateGame(data, ChessGame.TeamColor.BLACK, "piglet");
        Assertions.assertEquals("{}", blackResult);
    }

    @Test
    void updateGameFail() throws SQLException, DataAccessException {
        sgd.createGame("hundred acre woods", null, null);
        GameData data = sgd.getGameByName("hundred acre woods");
        sgd.updateGame(data, ChessGame.TeamColor.WHITE, "winnie");
        String whiteResult = sgd.updateGame(data, ChessGame.TeamColor.WHITE, "piglet");
        Assertions.assertEquals("taken", whiteResult);
        sgd.updateGame(data, ChessGame.TeamColor.BLACK, "piglet");
        String blackResult = sgd.updateGame(data, ChessGame.TeamColor.BLACK, "rabbit");
        Assertions.assertEquals("taken", blackResult);
    }

    @Test
    void clear() throws SQLException, DataAccessException {
        sgd.clear();
        Collection<GameData> result = sgd.listGames();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getGames() throws SQLException, DataAccessException {
        sgd.createGame("hundred acre woods", "winnie", null);
        HashMap<Integer, GameData> actual = sgd.getGames();
        Assertions.assertNotNull(actual);
    }

    @Test
    void getGameByName() throws SQLException, DataAccessException {
        sgd.createGame("hundred acre woods", "winnie", null);
        Assertions.assertNull(sgd.getGameByName("tournament"));
    }

    @Test
    void getGameByNameFail() throws SQLException, DataAccessException {
        sgd.createGame("hundred acre woods", "winnie", null);
        GameData actual = sgd.getGameByName("hundred acre woods");
        Assertions.assertEquals("winnie", actual.getWhiteUsername());
    }
}