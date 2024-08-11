package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.WebSocketService;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Set;

@WebSocket
public class WebSocketHandler {

    WebSocketSessions sessionsSet = new WebSocketSessions();
    //make a websocket service class in server to get all the data access
    WebSocketService service = new WebSocketService();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException {
        //listens for message from client
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getGameID(), session, command.getAuthToken());
            case MAKE_MOVE -> makeMove(command.getGameID(), command.getAuthToken(), session, message, command.getMove());
            case LEAVE -> leave(command.getGameID(), session, command.getAuthToken());
            case RESIGN -> resign(command.getGameID(), command.getAuthToken(), session);
        }
    }

    private void connect(int gameID, Session session, String authToken) throws DataAccessException, IOException {
        ServerMessage message = service.connect(gameID, session, authToken, sessionsSet);
        String json = new Gson().toJson(message);
        broadcastMessage(gameID, json, session);
    }

    private void makeMove(int gameID, String authToken, Session session, String message, ChessMove move){
        //make a move
    }

    private void leave(int gameID, Session session, String authToken) throws DataAccessException, IOException {
        //leave the game
        ServerMessage message = service.leave(gameID, session, authToken, sessionsSet);
        String json = new Gson().toJson(message);
        broadcastMessage(gameID, json, session);
    }

    private void resign(int gameID, String authToken, Session session) throws DataAccessException, IOException {
        //resign game
        ServerMessage message = service.resign(gameID, authToken);
        String json = new Gson().toJson(message);
        broadcastMessage(gameID, json, session);
    }

    public void sendMessage(String message, Session session) throws IOException {
        session.getRemote().sendString(message);
    }

    public void broadcastMessage(int gameID, String message, Session exceptThisSession) throws IOException {
        Set<Session> sessions = sessionsSet.getSessionsFromGame(gameID);
        ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
        for (Session ses : sessions){
            if (ses != exceptThisSession){
                sendMessage(message, ses);
            }
        }
    }
}
