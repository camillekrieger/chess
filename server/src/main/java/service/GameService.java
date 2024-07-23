package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class GameService {
    GameDAO gameDAO = new MemoryGameDAO();

    public Collection<GameData> ListGames(String authToken) throws DataAccessException {
        AuthDAO authDAO = UserService.getAuthDao();
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            return gameDAO.listGames();
        }
        return null;
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

    public void clear() throws DataAccessException {
        gameDAO.clear();
    }

    public HashMap<Integer, GameData> getGames(){
        return gameDAO.getGames();
    }
}
