package ui;

import chess.ChessGame;

public class JoinGameRequest {
    private String authToken;
    private ChessGame.TeamColor color;
    private int gameID;

    public JoinGameRequest(String authToken, ChessGame.TeamColor color, int gameID){
        this.authToken = authToken;
        this.color = color;
        this.gameID = gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

    public void setColor(ChessGame.TeamColor color) {
        this.color = color;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
