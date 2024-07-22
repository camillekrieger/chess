package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class GameService {
    public Collection<GameData> ListGames() throws DataAccessException {
        MemoryGameDAO mgd = new MemoryGameDAO();
        return mgd.listGames();
    }
    public int createGame(UserData WhiteUser, UserData BlackUser, String gameName) throws DataAccessException {
        MemoryGameDAO mgd = new MemoryGameDAO();
        return mgd.createGame(gameName, WhiteUser.getUsername(), BlackUser.getUsername());
    }

    public void joinGame(GameData game, String username) throws DataAccessException {
        int gameID = game.getGameID();
        MemoryGameDAO mgd = new MemoryGameDAO();
        mgd.updateGame(gameID, username);
    }
}
