package dataaccess;

import model.GameData;

import java.util.Collection;

public class MemoryGameDAO implements GameDAO{
    @Override
    public void createGame(String gameName) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
