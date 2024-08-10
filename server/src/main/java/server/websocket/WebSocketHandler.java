package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getGameID(), session, message);
            case MAKE_MOVE -> makeMove(command.getGameID(), command.getAuthToken(), session, message, command.getMove());
            case LEAVE -> leave(command.getGameID(), session, message);
            case RESIGN -> resign(command.getGameID(), message);
        }
    }

    private void connect(int gameID, Session session, String message){
        //join or observe a game
    }

    private void makeMove(int gameID, String authToken, Session session, String message, ChessMove move){
        //make a move
    }

    private void leave(int gameID, Session session, String message){
        //leave the game
    }

    private void resign(int gameID, String message){
        //resign game
    }
}
