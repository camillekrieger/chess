package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        UserData newUser = new UserData(username, password, email);
        users.put(username, newUser);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public void clear() throws DataAccessException {
        users = new HashMap<>();
    }

    public HashMap<String, UserData> getUsers(){
        return users;
    }
}
