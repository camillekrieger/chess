package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    public AuthData register(UserData user) throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        String username = user.getUsername();
        String ad = authDAO.createAuth(username);
        return authDAO.getAuth(ad);
    }
    public AuthData login(UserData user) throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        String username = user.getUsername();
        String ad = authDAO.createAuth(username);
        return authDAO.getAuth(ad);
    }
    public void logout(UserData user) throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        String authToken = authDAO.getToken(user.getUsername());
        authDAO.deleteAuth(authToken);
    }
}
