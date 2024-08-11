package client.websocket;

import chess.ChessGame;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import javax.management.Notification;

public interface NotificationHandler {
    void updateGame(ChessGame game);
    void printMessage(String message);
}
