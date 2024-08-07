package server;

import chess.ChessGame;

public class ListResult {
    int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    ChessGame game;

    public ListResult(int id, String wUser, String bUser, String gName, ChessGame game){
        this.gameID = id;
        this.whiteUsername = wUser;
        this.blackUsername = bUser;
        this.gameName = gName;
        this.game = game;
    }
}
