package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import javax.management.Notification;

@WebSocket
public class WebSocketHandler {

    WebSocketSessions sessionsSet = new WebSocketSessions();

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getGameID(), session);
            case MAKE_MOVE -> makeMove(command.getGameID(), command.getAuthToken(), session, message, command.getMove());
            case LEAVE -> leave(command.getGameID(), session, message);
            case RESIGN -> resign(command.getGameID(), message);
        }
    }

    private void connect(int gameID, Session session){
        //join or observe a game
        sessionsSet.addSessionToGame(gameID, session);
    }

    private void makeMove(int gameID, String authToken, Session session, String message, ChessMove move){
        //make a move
    }

    private void leave(int gameID, Session session, String message){
        //leave the game
        sessionsSet.removeSessionFromGame(gameID, session);
    }

    private void resign(int gameID, String message){
        //resign game
    }

    public void sendMessage(String message, Session session){
        //do stuff
    }

    public void broadcastMessage(int gameID, String message, Session exceptThisSession){
        //do stuff
    }
}
