package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private int nextGameID = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();
    @Override
    public void createGame(String gameName, String whiteUser, String blackUser) throws DataAccessException {
        ChessGame newGame = new ChessGame();
        GameData createdGame = new GameData(nextGameID, whiteUser, blackUser, gameName, newGame);
        games.put(nextGameID, createdGame);
        nextGameID++;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public void updateGame(int gameID) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
