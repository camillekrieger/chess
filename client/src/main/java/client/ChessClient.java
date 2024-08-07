package client;

import chess.ChessGame;
import model.GameData;
import serverfacade.ServerFacade;
import ui.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class ChessClient {
    private State state;
    private final ServerFacade server;
    private GamePlayUI gamePlay;

    public ChessClient(int serverURL, State state) throws IOException {
        this.server = new ServerFacade(serverURL);
        this.state = state;
        server.clear();
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
                case "logout" -> logout(params);
                case "help" -> this.help();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws IOException {
        if (params.length >= 1) {
            state = State.LOGGED_IN;
            server.register(params[0], params[1], params[2]);
            return String.format("Logged in as %s.", params[0]);
        }
        throw new IOException("Username already taken.");
    }

    public String login(String... params) throws URISyntaxException, IOException {
        if (params.length >= 1) {
            state = State.LOGGED_IN;
            server.login(params[0], params[1]);
            return String.format("Logged in as %s.", params[0]);
        }
        throw new IOException("Wrong login credentials.");
    }

    public String createGame(String... params) throws IOException {
        if (params.length >= 1) {
            CreateGameResponse response = server.createGame(params[0]);
            if (response.getGameID() == 0 || response.getGameID() == -1){
                return response.getMessage();
            }
            return String.format("Created %s game with %d id.", params[0], response.getGameID());
        }
        throw new IOException("Invalid game name.");
    }

    public String listGames(String... params) throws IOException {
        if (params.length >= 1) {
            ListGamesResponse response = server.listGames();
            if (response.getGames().isEmpty()){
                return response.getMessage();
            }
            else{
                for (GameData game : response.getGames()){
                    System.out.println(game);
                }
                return "These are the current games.";
            }
        }
        throw new IOException("There are no current games.");
    }

    public String joinGame(String... params) throws IOException {
        String c;
        if (params.length >= 1) {
            state = State.PLAYGAME;
            if ("WHITE".equals(params[1])){
                ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
                int gameID = Integer.parseInt(params[0]);
                server.joinGame(color, gameID);
                c = "White";
            }
            else{
                ChessGame.TeamColor color = ChessGame.TeamColor.BLACK;
                int gameID = Integer.parseInt(params[0]);
                server.joinGame(color, gameID);
                c = "Black";
            }
            gamePlay.draw();
            return String.format("You have joined the game as %s", c);
        }
        throw new IOException("Invalid credentials");
    }

    public String observeGame(String... params) throws IOException {
        if (params.length >= 1) {
            gamePlay.draw();
            return "You are now observing a game.";
        }
        throw new IOException("Game does not exist.");
    }

    public String logout(String... params) throws IOException {
        if (params.length >= 1) {
            state = State.LOGGED_OUT;
            server.logout();
            //if it is null that means you are logged out
            return "You are logged out";
        }
        throw new IOException("You are not logged out.");
    }
}
