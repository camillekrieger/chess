package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.WebSocketSessions;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class WebSocketService {

    AuthDAO authDAO = UserService.getAuthDao();
    GameDAO gameDAO = GameService.getGameDAO();

    public ServerMessage connect(int gameID, Session session, String authToken, WebSocketSessions sessionsSet) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            sessionsSet.addSessionToGame(gameID, session);
            GameData gameData = gameDAO.getGame(gameID);
            LoadGameMessage lgm = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.getGame());
            String user = authData.getUsername();
            String color;
            if (user.equals(gameData.getWhiteUsername())){
                color = "white";
            }
            else if (user.equals(gameData.getBlackUsername())){
                color = "black";
            }
            else{
                color = "observing";
            }
            String message;
            if(color.equals("observing")){
                message = String.format("%s joined %s as an observer", user, gameData.getGameName());
            }
            else {
                message = String.format("%s joined %s as %s", user, gameData.getGameName(), color);
            }
            return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        }
        return null;
    }
}
