package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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
    private String currColor;
    private String currGameNum;
    private boolean observing;
    private ChessGame currGame;

    public ChessClient(int serverURL, State state) {
        this.server = new ServerFacade(serverURL);
        this.state = state;
        this.numToID = new HashMap<>();
    }

    public String help(){
        if (state == State.LOGGED_OUT){
            PreloginUI preLog = new PreloginUI();
            return preLog.help();
        }
        else if (state == State.PLAYGAME){
            ChessGame game = new ChessGame();
            gamePlay = new GamePlayUI(game);
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

    public String makeMove(String... params){
        try{
            //make the move

        }
        catch(Exception e){
            return "Invalid move.";
        }
        return null;
    }

    public String legalMoves(String... params) {
        int row = Integer.parseInt(params[0]);
        int col = letterToNum(params[1]);
        if (col == 0 || row < 1 || row > 8){
            return "Not valid input.";
        }
        else {
            ChessPosition currPos = new ChessPosition(row, col);
            Collection<ChessMove> validMoves = currGame.validMoves(currPos);
            gamePlay.drawLegalMoves(currPos, validMoves, currColor);
            return "These are your legal moves.";
        }
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

    public String resign(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you wish to resign? [Y/N] >>> ");
        String result = scanner.nextLine();
        if (result.equals("y") || result.equals("yes")){
            String winner;
            if (currColor.equals("White")){
                winner = "Black";
            }
            else{
                winner = "White";
            }
            return String.format("Game Over. %s wins.", winner);
        }
        else {
            gamePlay.redrawBoard(currColor);
            return "Continue game play";
        }
    }

    public String redrawBoard(){
        gamePlay.redrawBoard(currColor);
        return "Here is the current game board.";
    }

    public String exit() throws IOException {
        if (!observing){
            int gameNum = Integer.parseInt(currGameNum);
            int gameID = numToID.get(gameNum);
            if (currColor.equals("White")){
                server.leaveGame(ChessGame.TeamColor.WHITE, gameID);
            }
            else{
                server.leaveGame(ChessGame.TeamColor.BLACK, gameID);
            }
        }
        state = State.LOGGED_IN;
        return "left game";
    }

    public String register(String... params) throws IOException {
        if (params.length == 3) {
            server.register(params[0], params[1], params[2]);
            state = State.LOGGED_IN;
            return String.format("Logged in as %s.", params[0]);
        }
        return null;
    }

    public String login(String... params) throws IOException {
        if (params.length == 2) {
            server.login(params[0], params[1]);
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
                    currColor = "White";
                }
                else {
                    server.joinGame(ChessGame.TeamColor.BLACK, gameID);
                    currColor = "Black";
                }
                currGame = new ChessGame();
                ListGamesResponse games = server.listGames();
                for (GameData game : games.getGames()) {
                    if (game.getGameID() == gameID) {
                        currGame = game.getGame();
                    }
                }
                gamePlay = new GamePlayUI(currGame);
                if (currColor.equals("White")){
                    gamePlay.drawWhite();
                }
                else {
                    gamePlay.drawBlack();
                }
                currGameNum = params[0];
                state = State.PLAYGAME;
                return String.format("You have joined the game as %s.", currColor);
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
                        gamePlay = new GamePlayUI(game.getGame());
                        name = game.getGameName();
                    }
                }
                gamePlay.drawWhite();
                state = State.PLAYGAME;
                observing = true;
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
