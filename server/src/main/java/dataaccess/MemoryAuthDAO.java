package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private static final UUID UUID = null;
    private final HashMap<String, AuthData> authData = new HashMap<>();

    @Override
    public String createAuth(String username) throws DataAccessException {
        String newAuth = java.util.UUID.randomUUID().toString();
        AuthData newAuthData = new AuthData(username, newAuth);
        authData.put(newAuth, newAuthData);
        return newAuth;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authData.get(authToken);
    }

    public String getToken(String username) throws DataAccessException{
        for (String token : authData.keySet()){
            if (authData.get(token).getUsername().equals(username)){
                return token;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authData.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        authData.clear();
    }
}
