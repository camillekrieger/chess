package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }
    public AuthData register(UserData user) throws DataAccessException {

    }
    public AuthData login(UserData user) {
        return null;
    }
    public void logout(UserData user) {}
}
