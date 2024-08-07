package ui;

import model.GameData;

import java.util.Collection;

public class ListGamesResponse {
    Collection<GameData> games;
    String message;

    public Collection<GameData> getGames() {
        return games;
    }

    public void setGames(Collection<GameData> games) {
        this.games = games;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
