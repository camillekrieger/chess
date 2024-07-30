package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() {}

    @Override
    public int createGame(String gameName, String whiteUsername, String blackUsername) throws DataAccessException {
        return 0;
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
    public String updateGame(GameData gameData, ChessGame.TeamColor color, String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public HashMap<Integer, GameData> getGames() {
        return null;
    }

    @Override
    public GameData getGameByName(String name) throws DataAccessException {
        return null;
    }

    @Override
    public int getPreviousID() {
        return 0;
    }
}
