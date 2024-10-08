package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import model.AuthData;
import model.GameData;
import serverfacade.ServerFacade;
import ui.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class ChessClient {
    private State state;
    private final ServerFacade server;
    private GamePlayUI gamePlay;
    private final HashMap <Integer, Integer> numToID;
    private ChessGame.TeamColor currColor;
    private String currGameNum;
    private boolean observing;
    private ChessGame currGame;
    private WebSocketFacade ws;
    private final int serverURL;
    private final NotificationHandler notificationHandler;
    private String currAuthToken;
    public ChessClient(int serverURL, State state, NotificationHandler notificationHandler) {
        this.server = new ServerFacade(serverURL);
        this.state = state;
        this.numToID = new HashMap<>();
        this.serverURL = serverURL;
        this.notificationHandler = notificationHandler;
    }

    public String help(){
        if (state == State.LOGGED_OUT){
            PreloginUI preLog = new PreloginUI();
            return preLog.help();
        }
        else if (state == State.PLAYGAME){
            gamePlay = new GamePlayUI();
            return gamePlay.help();
        }
        else{
            PostloginUI postLog = new PostloginUI();
            return postLog.help();
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                case "leave" -> exit();
                case "redraw" -> redrawBoard();
                case "resign" -> resign();
                case "move" -> makeMove(params);
                case "legal" -> legalMoves(params);
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public void setCurrGame(ChessGame game){
        this.currGame = game;
    }

    private ChessPiece.PieceType findPromo(ChessPosition start){
        ChessPiece.PieceType promo = null;
        ChessPiece.PieceType currPiece = currGame.getBoard().getPiece(start).getPieceType();
        if (currPiece.equals(ChessPiece.PieceType.PAWN)){
            Scanner scanner = new Scanner(System.in);
            var result = "";
            System.out.print("\nDesired Promotion Piece [QUEEN|KNIGHT|BISHOP|ROOK] >>> ");
            result = scanner.nextLine();
            promo = switch(result){
                case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
                case "BISHOP" -> ChessPiece.PieceType.BISHOP;
                case "ROOK" -> ChessPiece.PieceType.ROOK;
                default -> ChessPiece.PieceType.QUEEN;
            };
        }
        return promo;
    }

    private String actuallyMakeMove(String... params) throws IOException {
        int startRow = Integer.parseInt(params[0]);
        int startCol = letterToNum(params[1]);
        int endRow = Integer.parseInt(params[2]);
        int endCol = letterToNum(params[3]);
        ChessPosition start = new ChessPosition(startRow, startCol);
        ChessPosition end = new ChessPosition(endRow, endCol);
        ChessPiece.PieceType promo = null;
        if (currGame.getBoard().getPiece(start) == null){
            return "There is no piece at that location to move. Try again";
        }
        //check the piece, check if it has a promotion and have that equal promo
        if(currColor == ChessGame.TeamColor.BLACK && endRow == 1) {
            promo = findPromo(start);
        }
        if(currColor == ChessGame.TeamColor.WHITE && endRow == 8) {
            promo = findPromo(start);
        }
        ChessMove move = new ChessMove(start, end, promo);
        if (currGame.getTeamTurn().equals(currColor)) {
            int gameID = numToID.get(Integer.parseInt(currGameNum));
            ws.makeMove(currAuthToken, gameID, move);
            String nextMove;
            if (currGame.getTeamTurn().equals(ChessGame.TeamColor.WHITE)) {
                nextMove = "Black";
            } else {
                nextMove = "White";
            }
            return String.format("%s's turn.", nextMove);
        }
        return null;
    }

    public String makeMove(String... params){
        try{
            if (params.length == 4) {
                if (observing){
                    return "Unauthorized";
                }
                else if (currGame.isGameOver()) {
                    return "Game Over. No further actions can be made";
                }
                else{
                    return actuallyMakeMove(params);
                }
            }
        }
        catch(Exception e){
            return "Invalid move.";
        }
        return null;
    }

    public String legalMoves(String... params) {
        if (params.length == 2) {
            int row = Integer.parseInt(params[0]);
            int col = letterToNum(params[1]);
            if (col == 0 || row < 1 || row > 8) {
                return "Not valid input.";
            } else {
                ChessPosition currPos = new ChessPosition(row, col);
                Collection<ChessMove> validMoves = currGame.validMoves(currPos);
                String color;
                if (currColor.equals(ChessGame.TeamColor.WHITE)){
                    color = "White";
                }
                else{
                    color = "Black";
                }
                gamePlay.drawLegalMoves(currPos, validMoves, color, currGame);
                return "These are your legal moves.";
            }
        }
        return null;
    }

    private int letterToNum(String letter){
        return switch (letter) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> 0;
        };
    }

    public String resign() throws Exception {
        if (observing){
            return "Unauthorized";
        }
        else if(currGame.isGameOver()) {
            return "Game Over, No further actions can be made.";
        }
        else{
            Scanner scanner = new Scanner(System.in);
            System.out.print("Do you wish to resign? [Y/N] >>> ");
            String result = scanner.nextLine();
            if (result.equals("y") || result.equals("yes")) {
                int gameID = numToID.get(Integer.parseInt(currGameNum));
                ws.resignGame(currAuthToken, gameID);
                return "Game Over";
            }
            else {
                String color;
                if (currColor.equals(ChessGame.TeamColor.WHITE)) {
                    color = "White";
                } else {
                    color = "Black";
                }
                gamePlay.redrawBoard(color, currGame);
                return "Continue game play";
            }
        }
    }

    public String redrawBoard(){
        if (observing){
            gamePlay.redrawBoard("White", currGame);
        }
        else {
            String color;
            if (currColor.equals(ChessGame.TeamColor.WHITE)) {
                color = "White";
            } else {
                color = "Black";
            }
            gamePlay.redrawBoard(color, currGame);
        }
        return "Here is the current game board.";
    }

    public String exit() throws Exception {
        if (!observing){
            int gameNum = Integer.parseInt(currGameNum);
            int gameID = numToID.get(gameNum);
            if (currColor.equals(ChessGame.TeamColor.WHITE)){
                server.leaveGame(ChessGame.TeamColor.WHITE, gameID);
            }
            else{
                server.leaveGame(ChessGame.TeamColor.BLACK, gameID);
            }
        }
        state = State.LOGGED_IN;
        int gameID = numToID.get(Integer.parseInt(currGameNum));
        ws.leaveGame(currAuthToken, gameID);
        ws = null;
        return "left game";
    }

    public String register(String... params) throws IOException {
        if (params.length == 3) {
            AuthData authData = server.register(params[0], params[1], params[2]);
            currAuthToken = authData.getAuthToken();
            state = State.LOGGED_IN;
            return String.format("Logged in as %s.", params[0]);
        }
        return null;
    }

    public String login(String... params) throws IOException {
        if (params.length == 2) {
            AuthData authData = server.login(params[0], params[1]);
            currAuthToken = authData.getAuthToken();
            state = State.LOGGED_IN;
            return String.format("Logged in as %s.", params[0]);
        }
        else{
            return null;
        }
    }

    public String createGame(String... params) throws IOException {
        if (params.length == 1) {
            CreateGameResponse response = server.createGame(params[0]);
            if (response.getGameID() == 0 || response.getGameID() == -1){
                return null;
            }
            return String.format("Created game: %s.", params[0]);
        }
        return null;
    }

    public String listGames() throws IOException {
        ListGamesResponse response = server.listGames();
        int i = 1;
        if (response.getGames().isEmpty()){
            return response.getMessage();
        }
        else{
            System.out.println("Current games:");
            for (GameData game : response.getGames()){
                int gameID = game.getGameID();
                String gameName = game.getGameName();
                String whitePlayer = game.getWhiteUsername();
                String blackPlayer = game.getBlackUsername();
                System.out.printf("%d\tNAME: %s\tWHITE PLAYER: %s\tBLACK PLAYER: %s", i, gameName, whitePlayer, blackPlayer);
                System.out.println();
                numToID.put(i, gameID);
                i++;
            }
            return "You can join a game with at least one null player, or a game you have already joined.";
        }
    }

    public String joinGame(String... params) {
        try {
            if (params.length == 2) {
                int gameNum = Integer.parseInt(params[0]);
                if (gameNum > numToID.size()) {
                    populateList();
                }
                int gameID = numToID.get(gameNum);
                if ("white".equals(params[1])) {
                    server.joinGame(ChessGame.TeamColor.WHITE, gameID);
                    currColor = ChessGame.TeamColor.WHITE;
                }
                else {
                    server.joinGame(ChessGame.TeamColor.BLACK, gameID);
                    currColor = ChessGame.TeamColor.BLACK;
                }
                ListGamesResponse games = server.listGames();
                for (GameData game : games.getGames()) {
                    if (game.getGameID() == gameID) {
                        currGame = game.getGame();
                    }
                }
                gamePlay = new GamePlayUI();
                currGameNum = params[0];
                state = State.PLAYGAME;
                String newURL = "http://localhost:" + serverURL;
                ws = new WebSocketFacade(newURL, notificationHandler);
                ws.connect(currAuthToken, gameID);
                String color;
                if (currGame.getTeamTurn().equals(ChessGame.TeamColor.WHITE)){
                    color = "White";
                }
                else{
                    color = "Black";
                }
                return String.format("You have joined the game as %s. It is %s's move.", currColor, color);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private void populateList() throws IOException {
        ListGamesResponse response = server.listGames();
        int i = 1;
        for (GameData game : response.getGames()){
            int gameID = game.getGameID();
            numToID.put(i, gameID);
            i++;
        }
    }

    public String observeGame(String... params) {
        try {
            String name = null;
            if (params.length == 1) {
                ListGamesResponse games = server.listGames();
                int gameNum = Integer.parseInt(params[0]);
                if (gameNum > numToID.size()) {
                    populateList();
                }
                int gameID = numToID.get(gameNum);
                for (GameData game : games.getGames()) {
                    if (game.getGameID() == gameID) {
                        gamePlay = new GamePlayUI();
                        name = game.getGameName();
                    }
                }
                gamePlay = new GamePlayUI();
                currGameNum = params[0];
                state = State.PLAYGAME;
                observing = true;
                String newURL = "http://localhost:" + serverURL;
                ws = new WebSocketFacade(newURL, notificationHandler);
                ws.connect(currAuthToken, gameID);
                return String.format("You are now observing %s.", name);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public String logout() throws IOException {
        server.logout();
        state = State.LOGGED_OUT;
        return "You are logged out.";
    }
}
