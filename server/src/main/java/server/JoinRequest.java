package server;

import chess.ChessGame;

public class JoinRequest {
    ChessGame.TeamColor playerColor;
    int gameID;

    public JoinRequest(ChessGame.TeamColor color, int id){
        this.playerColor = color;
        this.gameID = id;
    }

    ChessGame.TeamColor getColor(){
        return playerColor;
    }

    Integer getGameID(){
        return gameID;
    }
}
