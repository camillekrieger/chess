package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class GameService {
    GameDAO gameDAO = new MemoryGameDAO();

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

    public void clear() throws DataAccessException {
        gameDAO.clear();
    }
}
