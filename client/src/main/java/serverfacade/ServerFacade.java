package serverfacade;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import ui.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class ServerFacade {
    private final int port;

    private String path = null;
    private String authToken;

    public ServerFacade(int port){
        this.port = port;
    }

    public void clear() throws IOException {
        path = "/db";
        makeRequest("DELETE", path, null, null,null);
    }

    public AuthData register(String username, String password, String email) throws IOException {
        path = "/user";
        UserData user = new UserData(username, password, email);
        AuthData authData = makeRequest("POST", path, user, null, AuthData.class);
        authToken = authData.getAuthToken();
        return authData;
    }

    public AuthData login(String username, String password) throws IOException {
        path = "/session";
        LoginRequest loginRequest = new LoginRequest(username, password);
        AuthData authData = makeRequest("POST", path, loginRequest, null, AuthData.class);
        authToken = authData.getAuthToken();
        return authData;
    }

    public void logout() throws IOException {
        path = "/session";
        makeRequest("DELETE", path, null, authToken, null);
    }

    public ListGamesResponse listGames() throws IOException {
        path = "/game";
        return makeRequest("GET", path, null, authToken, ListGamesResponse.class);
    }

    public CreateGameResponse createGame(String gameName) throws IOException {
        path = "/game";
        CreateGameRequest cgr = new CreateGameRequest(gameName);
        //make this a class
        return makeRequest("POST", path, cgr, authToken, CreateGameResponse.class);
    }

    public void joinGame(ChessGame.TeamColor color, int gameID) throws IOException {
        path = "/game";
        JoinGameRequest joinGameRequest = new JoinGameRequest(color, gameID);
        makeRequest("PUT", path, joinGameRequest, authToken, null);
    }

    public void leaveGame(ChessGame.TeamColor color, int gameID) throws IOException {
        path = "/game";
        JoinGameRequest leaveGameRequest = new JoinGameRequest(color, gameID);
        makeRequest("DELETE", path, leaveGameRequest, authToken, null);
    }

    private <T> T makeRequest(String method, String path, Object request, String headerValue, Class<T> response) throws IOException {
        try {
            String scheme = "http";
            String host = "localhost";
            URL url = (new URI(scheme, null, host, port, path, null, null)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (headerValue != null){
                http.setRequestProperty("authorization", headerValue);
            }
            if (request != null) {
                writeBody(request, http);
            }
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, response);
        } catch (Exception e){
            throw new IOException(e.getMessage());
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new IOException();
        }
    }

    private boolean isSuccessful(int status){
        return status == 200;
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
