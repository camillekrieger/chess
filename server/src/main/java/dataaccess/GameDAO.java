package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public interface GameDAO {

    int createGame(String gameName, String whiteUsername, String blackUsername) throws DataAccessException, SQLException;

    GameData getGame(int gameID) throws  DataAccessException;

    Collection<GameData> listGames() throws  DataAccessException;

    String updateGame(GameData gameData, ChessGame.TeamColor color,String username) throws DataAccessException;

    void clear() throws DataAccessException, SQLException;

    HashMap<Integer, GameData> getGames() throws DataAccessException;

    GameData getGameByName(String name) throws DataAccessException;

    int getPreviousID();
}
