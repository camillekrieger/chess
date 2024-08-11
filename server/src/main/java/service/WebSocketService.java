package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
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
            String gameJson = new Gson().toJson(gameData.getGameName());
            LoadGameMessage lgm = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson);
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

    public ServerMessage leave(int gameID, Session session, String authToken, WebSocketSessions sessionsSet) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            sessionsSet.removeSessionFromGame(gameID, session);
            GameData gameData = gameDAO.getGame(gameID);
            String message = String.format("%s left %s", authData.getUsername(), gameData.getGame());
            return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        }
        return null;
    }

    public ServerMessage resign(int gameID, String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            GameData gameData = gameDAO.getGame(gameID);
            String message = String.format("%s forfeits %s. Game Over.", authData.getUsername(), gameData.getGameName());
            return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        }
        return null;
    }

    public ServerMessage makeMove(int gameID, String authToken, ChessMove move) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            GameData gameData = gameDAO.getGame(gameID);
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();
            ChessPiece piece = gameData.getGame().getBoard().getPiece(start);
            String user = authData.getUsername();
            String pieceType = piece.getPieceType().toString();
            int startRow = start.getRow();
            String startCol = intToLet(start.getColumn());
            int endRow = end.getRow();
            String endCol = intToLet(end.getColumn());
            String message = String.format("%s moved %s at [%d,%s] to [%d,%s].", user, pieceType, startRow, startCol, endRow, endCol);
            return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        }
        return null;
    }

    private String intToLet(int col){
        return switch (col) {
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            case 8 -> "h";
            default -> "invalid";
        };
    }

    public ChessGame getGame(int gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        return gameData.getGame();
    }
}
