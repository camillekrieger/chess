package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.WebSocketService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.management.Notification;

@WebSocket
public class WebSocketHandler {

    WebSocketSessions sessionsSet = new WebSocketSessions();
    //make a websocket service class in server to get all the data access
    WebSocketService service = new WebSocketService();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException {
        //listens for message from client
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getGameID(), session, command.getAuthToken());
            case MAKE_MOVE -> makeMove(command.getGameID(), command.getAuthToken(), session, message, command.getMove());
            case LEAVE -> leave(command.getGameID(), session, message);
            case RESIGN -> resign(command.getGameID(), message);
        }
    }

    private ServerMessage connect(int gameID, Session session, String authToken) throws DataAccessException {
        ServerMessage message = service.connect(gameID, session, authToken, sessionsSet);
        String json = new Gson().toJson(message);
        broadcastMessage(gameID, json, session);
        return message;
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
