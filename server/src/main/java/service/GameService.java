package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public class GameService {
    static GameDAO gameDAO = new MemoryGameDAO();
    AuthDAO authDAO = UserService.getAuthDao();

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            return gameDAO.listGames();
        }
        return null;
    }

    public int createGame(String authToken, String gameName) throws DataAccessException, SQLException {
        AuthData authdata = authDAO.getAuth(authToken);
        if (authdata != null){
            if (gameDAO.getGameByName(gameName) == null){
                return gameDAO.createGame(gameName, null, null);
            }
            else{
                return -1;
            }
        }
        return 0;
    }

    public String joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null) {
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData != null) {
                String username = authData.getUsername();
                return gameDAO.updateGame(gameData, color, username);
            } else {
                return null;
            }
        }
        else{
            return null;
        }
    }

    public HashMap<Integer, GameData> getGames() throws DataAccessException {
        return gameDAO.getGames();
    }

    public static GameDAO getGameDAO() {
        return gameDAO;
    }

    public int getPreviousGameID(){
        return gameDAO.getPreviousID();
    }
}
