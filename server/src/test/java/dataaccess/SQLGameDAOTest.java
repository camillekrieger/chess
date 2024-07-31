package dataaccess;

import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;

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
    void clear() {
    }

    @Test
    void getGames() {
    }

    @Test
    void getGameByName() {
    }

    @Test
    void getPreviousID() {
    }
}