package serverfacade;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import ui.CreateGameRequest;
import ui.JoinGameRequest;
import ui.LoginRequest;

import java.util.Collection;

public class ServerFacade {

    private final String url;

    private String path = null;

    public ServerFacade(String url){
        this.url = url;
    }

    public void clear(){
        path = "/db";
        makeRequest("DELETE", path, null, null);
    }

    public Object register(UserData user){
        path = "/user";
        return makeRequest("POST", path, user, AuthData.class);
    }

    public Object login(String username, String password){
        path = "/session";
        LoginRequest loginRequest = new LoginRequest(username, password);
        return makeRequest("POST", path, loginRequest, AuthData.class);
    }

    public Object logout(String authToken){
        path = "/session";
        return makeRequest("DELETE", path, authToken, null);
    }

    public Object listGames(String authToken){
        path = "/game";
        record listGamesResponse(GameData[] gameData) {
        }
        return makeRequest("GET", path, authToken, listGamesResponse.class);
    }

    public Object createGame(String authToken, String gameName){
        path = "/game";
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
        return makeRequest("POST", path, createGameRequest, int.class);
    }

    public Object joinGame(String authToken, ChessGame.TeamColor color, int gameID){
        path = "/game";
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, color, gameID);
        return makeRequest("PUT", path, joinGameRequest, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Object response){
        return null;
    }

    private static void writeBody(){}

    private static <T> T readBody(){
        return null;
    }
}
