package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameService {
    static GameDAO gameDAO = new MemoryGameDAO();
    AuthDAO authDAO = UserService.getAuthDao();

    public Collection<GameData> ListGames(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            return gameDAO.listGames();
        }
        return null;
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        AuthData authdata = authDAO.getAuth(authToken);
        if (authdata != null){
            if (gameDAO.getGameByName(gameName) == null){
                return gameDAO.createGame(gameName, authdata.getUsername(), null);
            }
            else{
                return -1;
            }
        }
        return 0;
    }

    public String joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData != null) {
                String username = authData.getUsername();
                gameDAO.updateGame(gameData, color, username);
                return "{}";
            }
            else{
                return null;
            }
        }
        return null;
    }

    public HashMap<Integer, GameData> getGames(){
        return gameDAO.getGames();
    }

    public GameData getByName(String gameName) throws DataAccessException {
        return gameDAO.getGameByName(gameName);
    }

    public static GameDAO getGameDAO() {
        return gameDAO;
    }
}
