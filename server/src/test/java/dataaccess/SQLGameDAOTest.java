package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

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
    void getGame() {
    }

    @Test
    void listGames() {
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