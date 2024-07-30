package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class UserService {
    static AuthDAO authDAO = new MemoryAuthDAO();
    static UserDAO userDAO = new MemoryUserDAO();

    public static AuthDAO getAuthDao() {
        return authDAO;
    }

    public static UserDAO getUserDAO(){
        return userDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        if (username != null && password != null && email != null) {
            if (userDAO.getUser(username) == null) {
                userDAO.createUser(username, password, email);
                String ad = authDAO.createAuth(username);
                return authDAO.getAuth(ad);
            }
            else{
                String ad = authDAO.createAuth(null);
                return authDAO.getAuth(ad);
            }
        }
        return null;
    }
    public AuthData login(String username, String password) throws DataAccessException {
        UserData user = userDAO.getUser(username);
        if (user != null){
            if(user.getPassword().equals(password)){
                String ad = authDAO.createAuth(username);
                return authDAO.getAuth(ad);
            }
            else{
                return null;
            }
        }
        return null;
    }

    public String logout(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            authDAO.deleteAuth(authToken);
            return "{}";
        }
        return null;
    }

    public HashMap<String, UserData> getUsers() throws DataAccessException {
        return userDAO.getUsers();
    }

    public HashMap<String, AuthData> getAuths() throws DataAccessException {
        return authDAO.getAuths();
    }
}
