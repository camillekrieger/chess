package dataaccess;

import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
        Assertions.assertEquals(sgd.getPreviousID(), id);
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
    void listGames() throws SQLException, DataAccessException {
        sgd.createGame("hundred acre woods", "winnie", null);
        Collection<GameData> list = sgd.listGames();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void updateGame() {
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
        Assertions.assertEquals("hundred acre woods", actual.get(sgd.getPreviousID()).getGameName());
    }

    @Test
    void getGameByName() throws SQLException, DataAccessException {
        sgd.createGame("hundred acre woods", "winnie", null);
        GameData actual = sgd.getGameByName("hundred acre woods");
        Assertions.assertEquals("winnie", actual.getWhiteUsername());
    }

    @Test
    void getPreviousID() {
    }
}