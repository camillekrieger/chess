package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public interface GameDAO {

    int createGame(String gameName, String whiteUsername, String blackUsername) throws  DataAccessException;

    GameData getGame(int gameID) throws  DataAccessException;

    Collection<GameData> listGames() throws  DataAccessException;

    void updateGame(GameData gameData, int gameID) throws DataAccessException;

    void clear() throws DataAccessException;

    HashMap<Integer, GameData> getGames();

    GameData getGameByName(String name) throws DataAccessException;
}
