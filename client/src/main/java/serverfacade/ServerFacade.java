package serverfacade;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import ui.CreateGameRequest;
import ui.JoinGameRequest;
import ui.LoginRequest;

import java.io.IOException;
import java.net.*;

public class ServerFacade {

    private final String serverURL;

    private String path = null;

    public ServerFacade(String url){
        this.serverURL = url;
    }

    public void clear() throws URISyntaxException, IOException {
        path = "/db";
        makeRequest("DELETE", path, null, null);
    }

    public Object register(UserData user) throws URISyntaxException, IOException {
        path = "/user";
        return makeRequest("POST", path, user, AuthData.class);
    }

    public Object login(String username, String password) throws URISyntaxException, IOException {
        path = "/session";
        LoginRequest loginRequest = new LoginRequest(username, password);
        return makeRequest("POST", path, loginRequest, AuthData.class);
    }

    public Object logout(String authToken) throws URISyntaxException, IOException {
        path = "/session";
        return makeRequest("DELETE", path, authToken, null);
    }

    public Object listGames(String authToken) throws URISyntaxException, IOException {
        path = "/game";
        record listGamesResponse(GameData[] gameData) {
        }
        return makeRequest("GET", path, authToken, listGamesResponse.class);
    }

    public Object createGame(String authToken, String gameName) throws URISyntaxException, IOException {
        path = "/game";
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
        return makeRequest("POST", path, createGameRequest, int.class);
    }

    public Object joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws URISyntaxException, IOException {
        path = "/game";
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, color, gameID);
        return makeRequest("PUT", path, joinGameRequest, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Object response) throws URISyntaxException, IOException {
        URL url = (new URI(serverURL + path)).toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);
        http.setDoOutput(true);
        writeBody();
        http.connect();
        return readBody();
    }

    private static void writeBody(){}

    private static <T> T readBody(){
        return null;
    }
}
