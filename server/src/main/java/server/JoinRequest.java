package server;

import chess.ChessGame;

public class JoinRequest {
    ChessGame.TeamColor color;
    Integer gameID;

    ChessGame.TeamColor getColor(){
        return color;
    }

    Integer getGameID(){
        return gameID;
    }
}
