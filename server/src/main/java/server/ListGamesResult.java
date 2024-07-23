package server;

import model.GameData;

import java.util.Collection;

public class ListGamesResult {
    Collection<ListResult> games;

    void addGame(GameData gameData){
        ListResult lr = new ListResult(gameData.getGameID(), gameData.getWhiteUsername(), gameData.getBlackUsername(), gameData.getGameName());
        games.add(lr);
    }

    Collection<ListResult> getGames(){
        return games;
    }
}
