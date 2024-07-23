package server;

import model.GameData;

import java.util.Collection;

public class ListGamesResult {
    Collection<ListResult> games;

    public ListGamesResult(){}

    Collection<ListResult> addGames(Collection<GameData> gameData){
        for (GameData gd : gameData) {
            ListResult lr = new ListResult(gd.getGameID(), gd.getWhiteUsername(), gd.getBlackUsername(), gd.getGameName());
            games.add(lr);
        }
        return games;
    }
}
