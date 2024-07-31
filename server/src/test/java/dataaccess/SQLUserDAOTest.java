package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {

    private final String[] expectedStatements = {
            """
            CREATE TABLE IF NOT EXISTS  expected (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              'email' varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    @Test
    void createUser() throws SQLException, DataAccessException {
        SQLUserDAO sud = new SQLUserDAO();
        sud.createUser("winnie", "honey", "wtp@hawoods.com");
        var statement = "INSERT INTO expectedStatements (username, password, email) VALUES (winnie, honey, wtp@hawoods.com)";
        var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(statement);
        ps.executeUpdate();
        Assertions.assertEquals(expectedStatements, sud.getTable());
    }

    @Test
    void getUser() {
    }

    @Test
    void clear() {
    }

    @Test
    void getUsers() {
    }
}