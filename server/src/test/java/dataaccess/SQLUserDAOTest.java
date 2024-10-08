package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class SQLUserDAOTest {

    SQLUserDAO sud;

    @BeforeEach
    void runBefore() throws SQLException, DataAccessException {
        sud = new SQLUserDAO();
        sud.clear();
    }

    @Test
    void createUser() throws DataAccessException {
        sud.createUser("winnie", "honey", "wtp@hawoods.com");
        List<String> expected = new ArrayList<>();
        expected.add("winnie");
        expected.add("honey");
        expected.add("wtp@hawoods.com");
        Assertions.assertEquals(expected, sud.getTable().get("winnie"));
    }

    @Test
    void createUserFail() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
                sud.createUser("winnie", "honey", "wtp@hawoods.com");
                sud.createUser("winnie", "bees", "thepooh@hawoods.com");
        });
    }

    @Test
    void getUser() throws DataAccessException {
        UserData info = new UserData("winnie", "honey", "wtp@hawoods.com");
        sud.createUser("winnie", "honey", "wtp@hawoods.com");
        UserData actual = sud.getUser("winnie");
        Assertions.assertEquals(info.getUsername(), actual.getUsername());
        Assertions.assertEquals(info.getPassword(), actual.getPassword());
        Assertions.assertEquals(info.getEmail(), actual.getEmail());
    }

    @Test
    void getUserFail() throws DataAccessException {
        sud.createUser("winnie", "honey", "wtp@hawoods.com");
        UserData info = sud.getUser("piglet");
        Assertions.assertNull(info);
    }

    @Test
    void clear() throws SQLException, DataAccessException {
        sud.clear();
        HashMap<String, List> result = sud.getTable();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getUsers() throws DataAccessException {
        UserData info = new UserData("winnie", "honey", "wtp@hawoods.com");
        sud.createUser("winnie", "honey", "wtp@hawoods.com");
        HashMap<String, UserData> actual = sud.getUsers();
        Assertions.assertEquals(actual.get("winnie").getPassword(), info.getPassword());
    }
}