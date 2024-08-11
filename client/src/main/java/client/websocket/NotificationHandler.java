package client.websocket;

import chess.ChessGame;
import websocket.messages.NotificationMessage;

import javax.management.Notification;

public interface NotificationHandler {
    void updateGame(NotificationMessage notificationMessage);
    void printMessage(String message);
}
