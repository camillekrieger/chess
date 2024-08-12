package server.websocket;

import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Set;

@WebSocket
public class WebSocketHandler {
    WebSocketSessions sessionsSet;
    WebSocketService service;

    public WebSocketHandler(){
        sessionsSet = new WebSocketSessions();
        service = new WebSocketService();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException, InvalidMoveException {
        //listens for message from client
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getGameID(), session, command.getAuthToken());
            case MAKE_MOVE -> makeMove(command.getGameID(), command.getAuthToken(), session, command.getMove());
            case LEAVE -> leave(command.getGameID(), session, command.getAuthToken());
            case RESIGN -> resign(command.getGameID(), command.getAuthToken(), session);
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int code, String reason){
        sessionsSet.removeSession(session);
    }

    private void connect(int gameID, Session session, String authToken) throws DataAccessException, IOException {
        ServerMessage message = service.connect(gameID, session, authToken, sessionsSet);
        if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)){
            String json = new Gson().toJson(message);
            sendMessage(json, session);
        }
        else {
            String json = new Gson().toJson(message);
            sendMessage(json, session);
            String notifyMessage = service.notifyConnectMessage(gameID, authToken);
            NotificationMessage nm = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notifyMessage);
            String notifyJson = new Gson().toJson(nm);
            broadcastMessage(gameID, notifyJson, session);
        }
    }

    private void makeMove(int gameID, String authToken, Session session, ChessMove move) throws IOException, DataAccessException, InvalidMoveException {
        boolean resignedGame = service.gameDAO.getGame(gameID).getGame().isGameOver();
        if (resignedGame) {
            ErrorMessage msg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Game Over. No more moves can be made.");
            String json = new Gson().toJson(msg);
            sendMessage(json, session);
        }
        else {
            ServerMessage message = service.makeMove(gameID, authToken, move);
            if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
                String json = new Gson().toJson(message);
                sendMessage(json, session);
            } else {
                String json = new Gson().toJson(message);
                sendMessage(json, session);
                broadcastMessage(gameID, json, session);
                String notifyMessage = service.notifyMakeMove(gameID, authToken, move);
                NotificationMessage nm = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notifyMessage);
                String notifyJson = new Gson().toJson(nm);
                broadcastMessage(gameID, notifyJson, session);
                //need to still check if in check, checkmate, or stalemate
                if (service.notifyCheckmate(gameID)){
                    String color = null;
                    if(service.getGame(gameID).getWhiteUsername().equals(service.getUsername(authToken))){
                        color = "White";
                    }
                    else if(service.getGame(gameID).getBlackUsername().equals(service.getUsername(authToken))){
                        color = "Black";
                    }
                    NotificationMessage nm2 = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s is in checkmate.", color));
                    String nnJson = new Gson().toJson(nm2);
                    sendMessage(nnJson, session);
                    broadcastMessage(gameID, nnJson, session);
                }
                else if (service.notifyCheck(gameID)){
                    String color = null;
                    if(service.getGame(gameID).getWhiteUsername().equals(service.getUsername(authToken))){
                        color = "Black";
                    }
                    else if(service.getGame(gameID).getBlackUsername().equals(service.getUsername(authToken))){
                        color = "White";
                    }
                    NotificationMessage nm1 = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s is in check.", color));
                    String newJson = new Gson().toJson(nm1);
                    sendMessage(newJson, session);
                    broadcastMessage(gameID, newJson, session);
                }
                else if (service.notifyStalemate(gameID)){
                    NotificationMessage nm3 = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Game in stalemate.");
                    String nnnJson = new Gson().toJson(nm3);
                    sendMessage(nnnJson, session);
                    broadcastMessage(gameID, nnnJson, session);
                }
            }
        }
    }

    private void leave(int gameID, Session session, String authToken) throws DataAccessException, IOException {
        ServerMessage message = service.leave(gameID, session, authToken, sessionsSet);
        String json = new Gson().toJson(message);
        if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)){
            sendMessage(json, session);
        }
        else {
            broadcastMessage(gameID, json, session);
        }
    }

    private void resign(int gameID, String authToken, Session session) throws DataAccessException, IOException {
        boolean resignedGame = service.gameDAO.getGame(gameID).getGame().isGameOver();
        if (resignedGame) {
            ErrorMessage msg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Game Over. No further actions can be made.");
            String json = new Gson().toJson(msg);
            sendMessage(json, session);
        }
        else {
            ServerMessage message = service.resign(gameID, authToken);
            if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
                String json = new Gson().toJson(message);
                sendMessage(json, session);
            } else {
                String json = new Gson().toJson(message);
                sendMessage(json, session);
                broadcastMessage(gameID, json, session);
            }
        }
    }

    public void sendMessage(String message, Session session) throws IOException {
        session.getRemote().sendString(message);
    }

    public void broadcastMessage(int gameID, String message, Session exceptThisSession) throws IOException {
        Set<Session> sessions = sessionsSet.getSessionsFromGame(gameID);
        for (Session ses : sessions){
            if (ses != exceptThisSession){
                sendMessage(message, ses);
            }
        }
    }
}
