package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

    void createGame(String gameName, String whiteUsername, String blackUsername) throws  DataAccessException;

    GameData getGame(int gameID) throws  DataAccessException;

    Collection<GameData> listGames() throws  DataAccessException;

    void updateGame(int gameID, String username) throws DataAccessException;

    void clear() throws DataAccessException;
}
