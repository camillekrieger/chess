package client;

import chess.ChessGame;
import serverfacade.ServerFacade;
import ui.GamePlayUI;
import ui.PostloginUI;
import ui.PreloginUI;
import ui.State;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class ChessClient {
    private State state = State.SIGNEDOUT;
    private final ServerFacade server;
    private String visitorName = null;
    private GamePlayUI gamePlay;

    public ChessClient(int serverURL){
        this.server = new ServerFacade(serverURL);
    }

    public String help(){
        if (state == State.SIGNEDOUT){
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
                case "logout" -> logout(params);
                case "help" -> this.help();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws URISyntaxException, IOException {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            server.register(params[0], params[1], params[2]);
            return String.format("You registered as %s.", params[0]);
        }
        throw new IOException();
    }

    public String login(String... params) throws URISyntaxException, IOException {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            visitorName = params[0];
            server.login(params[0], params[1]);
            return String.format("You are signed in as %s.", visitorName);
        }
        throw new IOException();
    }

    public String createGame(String... params) throws URISyntaxException, IOException {
        if (params.length >= 1) {
            server.createGame(params[0]);
            return "You created a game";
        }
        throw new IOException();
    }

    public String listGames(String... params) throws URISyntaxException, IOException {
        if (params.length >= 1) {
            server.listGames();
            return "These are the current games.";
        }
        throw new IOException();
    }

    public String joinGame(String... params) throws URISyntaxException, IOException {
        String c;
        if (params.length >= 1) {
            state = State.PLAYGAME;
            if ("WHITE".equals(params[1])){
                ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
                int gameID = Integer.parseInt(params[2]);
                server.joinGame(color, gameID);
                c = "White";
            }
            else{
                ChessGame.TeamColor color = ChessGame.TeamColor.BLACK;
                int gameID = Integer.parseInt(params[2]);
                server.joinGame(color, gameID);
                c = "Black";
            }
            gamePlay.draw();
            return String.format("You have joined the game as %s", c);
        }
        throw new IOException();
    }

    public String observeGame(String... params) throws URISyntaxException, IOException {
        if (params.length >= 1) {
            gamePlay.draw();
            return "You are now observing a game.";
        }
        throw new IOException();
    }

    public String logout(String... params) throws URISyntaxException, IOException {
        if (params.length >= 1) {
            state = State.SIGNEDOUT;
            visitorName = String.join("-", params);
            server.logout();
            return "You are logged out";
        }
        throw new IOException();
    }
}
