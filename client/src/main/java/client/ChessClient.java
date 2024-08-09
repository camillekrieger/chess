package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import serverfacade.ServerFacade;
import ui.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;

public class ChessClient {
    private State state;
    private final ServerFacade server;
    private GamePlayUI gamePlay;
    private HashMap <Integer, Integer> numToID;

    public ChessClient(int serverURL, State state) throws IOException {
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
                case "help" -> this.help();
                case "quit" -> "quit";
                case "leave" -> exit();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }


    public String exit(){
        state = State.LOGGED_IN;
        return "leave game";
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
            return "You can join a game with at least one null player.";
        }
    }

    public String joinGame(String... params) throws IOException {
        try {
            String c;
            if (params.length == 2) {
                int gameNum = Integer.parseInt(params[0]);
                int gameID = numToID.get(gameNum);
                if ("white".equals(params[1])) {
                    server.joinGame(ChessGame.TeamColor.WHITE, gameID);
                    c = "White";
                } else {
                    server.joinGame(ChessGame.TeamColor.BLACK, gameID);
                    c = "Black";
                }
                ChessGame currGame = new ChessGame();
                ListGamesResponse games = server.listGames();
                for (GameData game : games.getGames()) {
                    if (game.getGameID() == gameID) {
                        currGame = game.getGame();
                    }
                }
                gamePlay = new GamePlayUI(currGame);
                if (c.equals("White")){
                    gamePlay.drawWhite();
                }
                else {
                    gamePlay.drawBlack();
                }
                state = State.PLAYGAME;
                return String.format("You have joined the game as %s.", c);
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

    public String observeGame(String... params) throws IOException {
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
