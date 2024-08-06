package serverfacade;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import ui.CreateGameRequest;
import ui.JoinGameRequest;
import ui.ListGamesResponse;
import ui.LoginRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Collection;
import java.util.List;

public class ServerFacade {
    private final int port;

    private String path = null;

    public ServerFacade(int port){
        this.port = port;
    }

    public void clear() throws URISyntaxException, IOException {
        path = "/db";
        makeRequest("DELETE", path, null, null);
    }

    public AuthData register(String username, String password, String email) throws URISyntaxException, IOException {
        path = "/user";
        UserData user = new UserData(username, password, email);
        return makeRequest("POST", path, user, AuthData.class);
    }

    public AuthData login(String username, String password) throws URISyntaxException, IOException {
        path = "/session";
        LoginRequest loginRequest = new LoginRequest(username, password);
        return makeRequest("POST", path, loginRequest, AuthData.class);
    }

    public void logout(String authToken) throws URISyntaxException, IOException {
        path = "/session";
        makeRequest("DELETE", path, authToken, null);
    }

    public ListGamesResponse listGames(String authToken) throws URISyntaxException, IOException {
        path = "/game";
        return makeRequest("GET", path, authToken, ListGamesResponse.class);
    }

    public int createGame(String authToken, String gameName) throws URISyntaxException, IOException {
        path = "/game";
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
        return makeRequest("POST", path, createGameRequest, int.class);
    }

    public void joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws URISyntaxException, IOException {
        path = "/game";
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, color, gameID);
        makeRequest("PUT", path, joinGameRequest, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> response) throws URISyntaxException, IOException {
        try {
            String scheme = "http";
            String host = "localhost";
            URL url = (new URI(scheme, null, host, port, path, null, null)).toURL();
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
