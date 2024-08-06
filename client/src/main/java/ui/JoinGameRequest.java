package ui;

import chess.ChessGame;

public class JoinGameRequest {
    private ChessGame.TeamColor color;
    private int gameID;

    public JoinGameRequest(ChessGame.TeamColor color, int gameID){
        this.color = color;
        this.gameID = gameID;
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
