package dataaccess;

import model.AuthData;

import java.util.UUID;

public interface AuthDAO {

    void createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void clear() throws DataAccessException;
}
