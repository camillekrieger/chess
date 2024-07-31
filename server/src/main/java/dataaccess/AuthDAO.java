package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.HashMap;

public interface AuthDAO {

    String createAuth(String username) throws DataAccessException, SQLException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException, SQLException;

    void clear() throws DataAccessException, SQLException;

    HashMap<String, AuthData> getAuths() throws DataAccessException;
}
