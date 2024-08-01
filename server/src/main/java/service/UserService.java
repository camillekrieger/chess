package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.HashMap;

public class UserService {
    static AuthDAO authDAO;

    static {
        try {
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static UserDAO userDAO;

    static {
        try {
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static AuthDAO getAuthDao() {
        return authDAO;
    }

    public static UserDAO getUserDAO(){
        return userDAO;
    }
    //put password hashing in this class
    public AuthData register(UserData user) throws DataAccessException, SQLException {
        String username = user.getUsername();
        String password = user.getPassword();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String email = user.getEmail();
        if (username != null && password != null && email != null) {
            if (userDAO.getUser(username) == null) {
                userDAO.createUser(username, hashedPassword, email);
                String ad = authDAO.createAuth(username);
                return authDAO.getAuth(ad);
            }
            else{
                String ad = authDAO.createAuth("taken");
                return authDAO.getAuth(ad);
            }
        }
        return null;
    }

    public AuthData login(String username, String password) throws DataAccessException, SQLException {
        UserData user = userDAO.getUser(username);
        if (user != null){
            if(BCrypt.checkpw(password, user.getPassword())){
                String ad = authDAO.createAuth(username);
                return authDAO.getAuth(ad);
            }
            else{
                return null;
            }
        }
        return null;
    }

    public String logout(String authToken) throws DataAccessException, SQLException {
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
