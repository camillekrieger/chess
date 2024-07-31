package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import java.sql.SQLException;

public class ClearService {
    AuthDAO authDAO = UserService.getAuthDao();
    GameDAO gameDAO = GameService.getGameDAO();
    UserDAO userDAO = UserService.getUserDAO();
    public void clear() throws DataAccessException, SQLException {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }
}
