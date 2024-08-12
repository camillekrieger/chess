package server.websocket;

import chess.*;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Collection;

public class WebSocketService {

    AuthDAO authDAO = UserService.getAuthDao();
    GameDAO gameDAO = GameService.getGameDAO();

    public ServerMessage connect(int gameID, Session session, String authToken, WebSocketSessions sessionsSet) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData != null) {
                sessionsSet.addSessionToGame(gameID, session);
                return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.getGame());
            }
            else {
                return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "invalid game id.");
            }
        }
        else {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "invalid auth token.");
        }
    }

    public String notifyConnectMessage(int gameID, String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null) {
            GameData gameData = gameDAO.getGame(gameID);
            String user = authData.getUsername();
            String color;
            if (user.equals(gameData.getWhiteUsername())) {
                color = "white";
            } else if (user.equals(gameData.getBlackUsername())) {
                color = "black";
            } else {
                color = "observing";
            }
            String message;
            if (color.equals("observing")) {
                message = String.format("%s joined %s as an observer", user, gameData.getGameName());
            } else {
                message = String.format("%s joined %s as %s", user, gameData.getGameName(), color);
            }
            return message;
        }
        return null;
    }

    public ServerMessage leave(int gameID, Session session, String authToken, WebSocketSessions sessionsSet) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData != null) {
                sessionsSet.removeSessionFromGame(gameID, session);
                ChessGame.TeamColor color;
                if(gameData.getWhiteUsername() != null && gameData.getWhiteUsername().equals(authData.getUsername())){
                    color = ChessGame.TeamColor.WHITE;
                }
                else{
                    color = ChessGame.TeamColor.BLACK;
                }
                gameDAO.removeUser(gameData, color, authData.getUsername());
                String message = String.format("%s left %s", authData.getUsername(), gameData.getGameName());
                return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            }
            else{
                return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "invalid game id.");
            }
        }
        else {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "invalid auth token.");
        }
    }

    public ServerMessage resign(int gameID, String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData != null) {
                if(authData.getUsername().equals(gameData.getBlackUsername()) || authData.getUsername().equals(gameData.getWhiteUsername())){
                    ChessGame newGame = gameData.getGame();
                    newGame.setGameOver(true);
                    gameDAO.updateChessGame(gameData.getGameID(), newGame);
                    String message = String.format("%s forfeits %s. Game Over.", authData.getUsername(), gameData.getGameName());
                    return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                }
                else{
                    return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Observers cannot resign the game");
                }
            }
            else{
                return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid game id.");
            }
        }
        else {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid auth token.");
        }
    }

    public ServerMessage makeMove(int gameID, String authToken, ChessMove move) throws DataAccessException, InvalidMoveException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null){
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData != null) {
                ChessGame.TeamColor currTurn = gameData.getGame().getTeamTurn();
                ChessGame.TeamColor player;
                if (authData.getUsername().equals(gameData.getWhiteUsername())){
                    player = ChessGame.TeamColor.WHITE;
                }
                else if (authData.getUsername().equals(gameData.getBlackUsername())){
                    player = ChessGame.TeamColor.BLACK;
                }
                else{
                    return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "observers cannot make moves.");
                }
                if(player.equals(currTurn)) {
                    Collection<ChessMove> moves = gameData.getGame().validMoves(move.getStartPosition());
                    for (ChessMove m : moves) {
                        if (m.equals(move)) {
                            ChessGame newGame = gameDAO.getGame(gameID).getGame();
                            newGame.makeMove(move);
                            gameDAO.updateChessGame(gameID, newGame);
                            return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, newGame);
                        }
                    }
                    return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "invalid move.");
                }
                else{
                    return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "it is not your turn.");
                }
            }
            else{
                return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "invalid game id.");
            }
        }
        else {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "invalid auth token.");
        }
    }

    public boolean notifyCheck(int gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame.TeamColor currTurn = gameData.getGame().getTeamTurn();
        return gameData.getGame().isInCheck(currTurn);
    }

    public boolean notifyCheckmate(int gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame.TeamColor currTurn = gameData.getGame().getTeamTurn();
        return gameData.getGame().isInCheckmate(currTurn);
    }

    public boolean notifyStalemate(int gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame.TeamColor currTurn = gameData.getGame().getTeamTurn();
        return gameData.getGame().isInStalemate(currTurn);
    }

    public String notifyMakeMove(int gameId, String authToken, ChessMove move) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData != null) {
            GameData gameData = gameDAO.getGame(gameId);
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();
            ChessPiece piece = gameData.getGame().getBoard().getPiece(end);
            String user = authData.getUsername();
            String pieceType = piece.getPieceType().toString();
            int startRow = start.getRow();
            String startCol = intToLet(start.getColumn());
            int endRow = end.getRow();
            String endCol = intToLet(end.getColumn());
            return String.format("%s moved %s at [%d,%s] to [%d,%s].", user, pieceType, startRow, startCol, endRow, endCol);
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

    public GameData getGame(int gameID) throws DataAccessException {
        return gameDAO.getGame(gameID);
    }

    public String getUsername(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        return authData.getUsername();
    }
}
