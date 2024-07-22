package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
    public AuthData register(UserData user) throws DataAccessException {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        String username = user.getUsername();
        if (userDAO.getUser(username) == null){
            userDAO.createUser(username, user.getPassword(), user.getEmail());
            String ad = authDAO.createAuth(username);
            return authDAO.getAuth(ad);
        }
        return null;
    }
    public AuthData login(UserData user) throws DataAccessException {
        AuthDAO authDAO = new MemoryAuthDAO();
        String username = user.getUsername();
        String ad = authDAO.createAuth(username);
        return authDAO.getAuth(ad);
    }
    public void logout(UserData user) throws DataAccessException {
        AuthDAO authDAO = new MemoryAuthDAO();
        String authToken = authDAO.getToken(user.getUsername());
        authDAO.deleteAuth(authToken);
    }
}
