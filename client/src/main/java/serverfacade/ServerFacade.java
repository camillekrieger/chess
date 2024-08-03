package serverfacade;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import ui.CreateGameRequest;
import ui.JoinGameRequest;
import ui.LoginRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

    private <T> T makeRequest(String method, String path, Object request, Class<T> response) throws URISyntaxException, IOException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, response);
        } catch (Exception e){
            throw new IOException();
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new IOException();
        }
    }

    private boolean isSuccessful(int status){
        return status / 100 == 2;
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null){
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
