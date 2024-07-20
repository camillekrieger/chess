package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        
    }
}
