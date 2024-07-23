package server;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class ListGamesResult {
    Collection<ListResult> games;

    public ListGamesResult(){
        this.games = new ArrayList<>();
    }

    void addGames(Collection<GameData> gameData){
        for (GameData gd : gameData) {
            ListResult lr = new ListResult(gd.getGameID(), gd.getWhiteUsername(), gd.getBlackUsername(), gd.getGameName());
            games.add(lr);
        }
    }

    Collection<ListResult> getGames(){
        return games;
    }
}
