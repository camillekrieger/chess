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
                case "exit" -> exit();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }


    public String exit(){
        state = State.LOGGED_IN;
        return "exit";
    }

    public String register(String... params) throws IOException {
        if (params.length >= 1) {
            server.register(params[0], params[1], params[2]);
            state = State.LOGGED_IN;
            return String.format("Logged in as %s.", params[0]);
        }
        throw new IOException("Username already taken.");
    }

    public String login(String... params) throws URISyntaxException, IOException {
        if (params.length >= 1) {
            server.login(params[0], params[1]);
            state = State.LOGGED_IN;
            return String.format("Logged in as %s.", params[0]);
        }
        throw new IOException("Wrong login credentials.");
    }

    public String createGame(String... params) throws IOException {
        if (params.length >= 1) {
            CreateGameResponse response = server.createGame(params[0]);
            if (response.getGameID() == 0 || response.getGameID() == -1){
                return "Invalid name or name already taken.";
            }
            return String.format("Created game name: %s with game id: %d.", params[0], response.getGameID());
        }
        throw new IOException("Invalid game name.");
    }

    public String listGames() throws IOException {
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

    public String joinGame(String... params) throws IOException {
        String c;
        if (params.length >= 1) {
            int gameID = Integer.parseInt(params[0]);
            if ("white".equals(params[1])){
                server.joinGame(ChessGame.TeamColor.WHITE, gameID);
                c = "White";
            }
            else{
                server.joinGame(ChessGame.TeamColor.BLACK, gameID);
                c = "Black";
            }
            ChessGame currGame = new ChessGame();
            ListGamesResponse games = server.listGames();
            for (GameData game : games.getGames()){
                if (game.getGameID() == gameID){
                    currGame = game.getGame();
                }
            }
            gamePlay = new GamePlayUI(currGame);
            gamePlay.draw();
            state = State.PLAYGAME;
            return String.format("You have joined the game as %s.", c);
        }
        throw new IOException("Invalid credentials");
    }

    public String observeGame(String... params) throws IOException {
        String name = null;
        if (params.length >= 1) {
            ListGamesResponse games = server.listGames();
            for (GameData game : games.getGames()){
                if (game.getGameID() == Integer.parseInt(params[0])){
                    gamePlay = new GamePlayUI(game.getGame());
                    name = game.getGameName();
                }
            }
            gamePlay.draw();
            state = State.PLAYGAME;
            return String.format("You are now observing %s game.", name);
        }
        throw new IOException("Game does not exist.");
    }

    public String logout() throws IOException {
        server.logout();
        state = State.LOGGED_OUT;
        return "You are logged out.";
    }
}
