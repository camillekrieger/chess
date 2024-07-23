package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private int nextGameID = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();
    @Override
    public int createGame(String gameName, String whiteUser, String blackUser) throws DataAccessException {
        ChessGame newGame = new ChessGame();
        GameData createdGame = new GameData(nextGameID, whiteUser, blackUser, gameName, newGame);
        games.put(nextGameID, createdGame);
        int temp = nextGameID;
        nextGameID++;
        return temp;
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
    public String updateGame(GameData gameData, ChessGame.TeamColor color, String username) throws DataAccessException {
       if (color == ChessGame.TeamColor.WHITE){
           if (gameData.getWhiteUsername() == null){
               gameData.setWhiteUsername(username);
               return "{}";
           }
           else if (gameData.getWhiteUsername().equals(username)){
               return "{}";
           }
           else{
               return "taken";
           }
       }
       if (color == ChessGame.TeamColor.BLACK){
           if (gameData.getBlackUsername() == null){
               gameData.setBlackUsername(username);
               return "{}";
           }
           else if (gameData.getBlackUsername().equals(username)){
               return "{}";
           }
           else{
               return "taken";
           }
       }
       return "no color";
    }

    public GameData getGameByName(String gameName) throws DataAccessException {
        for (Integer key : games.keySet()){
            if (games.get(key).getGameName().equals(gameName)){
                return games.get(key);
            }
            else{
                return null;
            }
        }
        return null;
    }

    @Override
    public int getPreviousID(){
        return nextGameID - 1;
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }

    @Override
    public HashMap<Integer, GameData> getGames(){
        return games;
    }
}
