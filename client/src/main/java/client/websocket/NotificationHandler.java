package client.websocket;

import chess.ChessGame;

import javax.management.Notification;

public interface NotificationHandler {
    void updateGame(ChessGame game);
    void printMessage(String message);
}
