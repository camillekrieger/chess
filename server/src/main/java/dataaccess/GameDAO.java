package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {

    public default void createGame(){
        ChessGame game = new ChessGame();
        //new GameData()
    }

    public GameData getGame(int gameID){
        //retrieve game
    }

    public Collection<GameData> listGames(){
        //do the thing
    }

    public default void updateGame(){
        //do a thing
    }
}
