package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class Service {
    AuthDAO authDAO = new MemoryAuthDAO();
    UserDAO userDAO = new MemoryUserDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    public AuthData register(UserData user) throws DataAccessException {
        String username = user.getUsername();
        if (userDAO.getUser(username) == null){
            userDAO.createUser(username, user.getPassword(), user.getEmail());
            String ad = authDAO.createAuth(username);
            return authDAO.getAuth(ad);
        }
        return null;
    }
    public AuthData login(UserData user) throws DataAccessException {
        String username = user.getUsername();
        String ad = authDAO.createAuth(username);
        return authDAO.getAuth(ad);
    }
    public void logout(UserData user) throws DataAccessException {
        String authToken = authDAO.getToken(user.getUsername());
        authDAO.deleteAuth(authToken);
    }

    public void clear() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    public HashMap<String, UserData> getUsers(){
        return userDAO.getUsers();
    }

    public Collection<GameData> ListGames() throws DataAccessException {
        return gameDAO.listGames();
    }

    public int createGame(UserData WhiteUser, UserData BlackUser, String gameName) throws DataAccessException {
        return gameDAO.createGame(gameName, WhiteUser.getUsername(), BlackUser.getUsername());
    }

    public void joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        if (color.equals(ChessGame.TeamColor.WHITE)){
            if (gameData.getWhiteUsername() == null){
                gameDAO.updateGame(gameData, gameID);
            }
        }
        else{
            if (gameData.getBlackUsername() == null){
                gameDAO.updateGame(gameData, gameID);
            }
        }
    }

    public HashMap<String, AuthData> getAuths(){
        return authDAO.getAuths();
    }
}
