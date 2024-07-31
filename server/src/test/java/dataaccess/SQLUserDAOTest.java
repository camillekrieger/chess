package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void getUser() throws DataAccessException {
        UserData info = new UserData("winnie", "honey", "wtp@hawoods.com");
        sud.createUser("winnie", "honey", "wtp@hawoods.com");
        UserData actual = sud.getUser("winnie");
        Assertions.assertEquals(info.getUsername(), actual.getUsername());
        Assertions.assertEquals(info.getPassword(), actual.getPassword());
        Assertions.assertEquals(info.getEmail(), actual.getEmail());
    }

    @Test
    void clear() throws SQLException, DataAccessException {
        sud.clear();
        HashMap<String, List> result = sud.getTable();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getUsers() {
    }
}