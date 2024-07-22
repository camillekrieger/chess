package dataaccess;

import model.AuthData;

public interface AuthDAO {

    String createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    String getToken(String username) throws DataAccessException;

    void clear() throws DataAccessException;
}
