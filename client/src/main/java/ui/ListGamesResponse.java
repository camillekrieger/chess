package ui;

import model.GameData;

import java.util.Collection;

public class ListGamesResponse {

    Collection<GameData> gameDataList;

    public ListGamesResponse(Collection<GameData> list){
        this.gameDataList = list;
    }

    public Collection<GameData> getGameDataList() {
        return gameDataList;
    }

    public void setGameDataList(Collection<GameData> gameDataList) {
        this.gameDataList = gameDataList;
    }
}
