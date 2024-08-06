package ui;

import chess.ChessGame;

public class JoinGameRequest {
    private ChessGame.TeamColor playerColor;
    private int gameID;

    public JoinGameRequest(ChessGame.TeamColor color, int gameID){
        this.playerColor = color;
        this.gameID = gameID;
    }

    public ChessGame.TeamColor getColor() {
        return playerColor;
    }

    public void setColor(ChessGame.TeamColor color) {
        this.playerColor = color;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
