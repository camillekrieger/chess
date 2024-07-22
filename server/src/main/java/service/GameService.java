package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class GameService {
    public Collection<GameData> ListGames() throws DataAccessException {
        GameDAO mgd = new MemoryGameDAO();
        return mgd.listGames();
    }
    public int createGame(UserData WhiteUser, UserData BlackUser, String gameName) throws DataAccessException {
        GameDAO mgd = new MemoryGameDAO();
        return mgd.createGame(gameName, WhiteUser.getUsername(), BlackUser.getUsername());
    }

    public void joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws DataAccessException {
        GameDAO mgd = new MemoryGameDAO();
        GameData gameData = mgd.getGame(gameID);
        if (color.equals(ChessGame.TeamColor.WHITE)){
            if (gameData.getWhiteUsername() == null){
                mgd.updateGame(gameData, gameID);
            }
        }
        else{
            if (gameData.getBlackUsername() == null){
                mgd.updateGame(gameData, gameID);
            }
        }
    }
}
