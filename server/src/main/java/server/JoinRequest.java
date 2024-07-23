package server;

import chess.ChessGame;

public class JoinRequest {
    ChessGame.TeamColor playerColor;
    Integer gameID;

    ChessGame.TeamColor getColor(){
        return playerColor;
    }

    Integer getGameID(){
        return gameID;
    }
}
