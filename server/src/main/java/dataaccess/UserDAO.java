package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public interface UserDAO {

    void createUser(String username, String password, String email) throws DataAccessException, SQLException;

    UserData getUser(String username) throws DataAccessException;

    void clear() throws DataAccessException, SQLException;

    HashMap<String, UserData> getUsers() throws DataAccessException;
}
