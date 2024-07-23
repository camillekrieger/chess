package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public interface UserDAO {

    void createUser(String username, String password, String email) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;

    HashMap<String, UserData> getUsers();
}
