package client.websocket;

import chess.ChessGame;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

import javax.management.Notification;

public interface NotificationHandler {
    void updateGame(UserGameCommand command);
    void printMessage(String message);
}
