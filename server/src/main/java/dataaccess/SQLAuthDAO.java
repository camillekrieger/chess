package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() {}

    @Override
    public String createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public HashMap<String, AuthData> getAuths() {
        return null;
    }
}
