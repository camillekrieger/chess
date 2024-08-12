package client.websocket;

import chess.ChessGame;
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
                    MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
//                    int gameID = command.getGameID();
//                    WebSocketService wss = new WebSocketService();
//                    ChessGame game = wss.getGame(gameID);
//                    notificationHandler.updateGame(game);
                    switch (command.getCommandType()){
                        case CONNECT -> {
                            try {
                                connect(command.getAuthToken(), command.getGameID());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case MAKE_MOVE -> {
                            try {
                                makeMove(command.getAuthToken(), command.getGameID(), command.getMove());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case LEAVE -> {
                            try {
                                leaveGame(command.getAuthToken(), command.getGameID());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case RESIGN -> {
                            try {
                                resignGame(command.getAuthToken(), command.getGameID());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
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
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        try {
            MakeMoveCommand command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }

    public void leaveGame(String authToken, int gameID) throws IOException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }

    public void resignGame(String authToken, int gameID) throws IOException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex){
            throw new IOException(ex);
        }
    }

    private void sendMessage(ServerMessage.ServerMessageType type, String message){
        switch(type){
            case ERROR -> notificationHandler.printMessage(new ErrorMessage(type, message).getErrorMessage());
            case LOAD_GAME -> notificationHandler.printMessage(new LoadGameMessage(type, message).getGame());
            case NOTIFICATION -> notificationHandler.printMessage(new NotificationMessage(type, message).getMessage());
        }
    }
}
