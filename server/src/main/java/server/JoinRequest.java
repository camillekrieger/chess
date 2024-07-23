package server;

import chess.ChessGame;

public class JoinRequest {
    ChessGame.TeamColor color;
    int gameID;

    ChessGame.TeamColor getColor(){
        return color;
    }

    int getGameID(){
        return gameID;
    }
}
