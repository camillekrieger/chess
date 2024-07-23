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
        if (userDAO.getUser(username) == null){
            userDAO.createUser(username, user.getPassword(), user.getEmail());
            String ad = authDAO.createAuth(username);
            return authDAO.getAuth(ad);
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

    public void logout(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            authDAO.deleteAuth(authToken);
        }
    }

    public HashMap<String, UserData> getUsers(){
        return userDAO.getUsers();
    }

    public HashMap<String, AuthData> getAuths(){
        return authDAO.getAuths();
    }
}
