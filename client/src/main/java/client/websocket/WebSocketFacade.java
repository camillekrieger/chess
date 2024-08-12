package client.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebSocketFacade extends Endpoint implements MessageHandler {
    Session session;
    NotificationHandler notificationHandler;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                    switch (msg.getServerMessageType()){
                        case LOAD_GAME-> {
                            LoadGameMessage lgm = new Gson().fromJson(message, LoadGameMessage.class);
                            notificationHandler.updateGame(lgm.getGame());
                        }
                        case NOTIFICATION -> {
                            NotificationMessage nm = new Gson().fromJson(message, NotificationMessage.class);
                            notificationHandler.printMessage(nm.getMessage());
                        }
                        case ERROR -> {
                            ErrorMessage em = new Gson().fromJson(message, ErrorMessage.class);
                            notificationHandler.printMessage(em.getErrorMessage());
                        }
                    }
                }
            });
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public void connect(String authToken, int gameID) throws IOException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            sendMessage(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        try {
            MakeMoveCommand command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            sendMessage(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }

    public void leaveGame(String authToken, int gameID) throws IOException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            sendMessage(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }

    public void resignGame(String authToken, int gameID) throws IOException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            sendMessage(new Gson().toJson(command));
        } catch (IOException ex){
            throw new IOException(ex);
        }
    }

    private void sendMessage(String command) throws IOException {
        this.session.getBasicRemote().sendText(command);
    }
}
