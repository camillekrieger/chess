package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {

    SQLAuthDAO sad;

    @BeforeEach
    void runBefore() throws SQLException, DataAccessException {
        sad = new SQLAuthDAO();
        sad.clear();
    }

    @Test
    void createAuth() throws SQLException, DataAccessException {
        String newAuth = sad.createAuth("winnie");
        AuthData result = sad.getAuth(newAuth);
        Assertions.assertEquals("winnie", result.getUsername());
    }

    @Test
    void getAuth() throws SQLException, DataAccessException {
        String newAuth = sad.createAuth("winnie");
        AuthData result = sad.getAuth(newAuth);
        Assertions.assertEquals("winnie", result.getUsername());
    }

    @Test
    void getAuthFail() throws SQLException, DataAccessException {
        sad.createAuth("winnie");
        AuthData result = sad.getAuth("1234");
        Assertions.assertNull(result);
    }

    @Test
    void deleteAuth() throws SQLException, DataAccessException {
        String token = sad.createAuth("winnie");
        sad.deleteAuth(token);
        Assertions.assertNull(sad.getAuth(token));
    }

    @Test
    void deleteAuthFail() throws SQLException, DataAccessException {
        String token = sad.createAuth("winnie");
        sad.deleteAuth("1234");
        Assertions.assertNotNull(sad.getAuth(token));
    }

    @Test
    void clear() {
    }

    @Test
    void getAuths() {
    }
}