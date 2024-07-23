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
    GameDAO gameDAO = new MemoryGameDAO();
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
                return 0;
            }
        }
        return 0;
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

    public void clear() throws DataAccessException {
        gameDAO.clear();
    }

    public HashMap<Integer, GameData> getGames(){
        return gameDAO.getGames();
    }

    public GameData getByName(String gameName) throws DataAccessException {
        return gameDAO.getGameByName(gameName);
    }
}
