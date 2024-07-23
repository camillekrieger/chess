package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Collection;

public class Server {

    UserService userService = new UserService();
    GameService gameService = new GameService();

    ClearService clearService = new ClearService();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::ClearHandler);
        Spark.post("/user", this::RegisterHandler);
        Spark.post("/session", this::LoginHandler);
        Spark.delete("/session", this::LogoutHandler);
        Spark.get("/game", this::ListGamesHandler);
        Spark.post("/game", this::CreateGameHandler);
        Spark.put("/game", this::JoinGameHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object ClearHandler(Request request, Response response) throws DataAccessException {
        String result = clearService.clear();
        return new Gson().toJson(result);
    }

    private Object RegisterHandler(Request request, Response response) throws DataAccessException {
        var serializer = new Gson();
        var info = serializer.fromJson(request.body(), UserData.class);
        AuthData authData = userService.register(info);
        if (authData == null){
            response.status(403);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: already taken");
            return new Gson().toJson(ec);
        }
        else {
            return new Gson().toJson(authData);
        }
    }

    private Object LoginHandler(Request request, Response response) throws DataAccessException {
        var serializer = new Gson();
        var info = serializer.fromJson(request.body(), UserData.class);
        System.out.println(info);
        AuthData authData = userService.login(info.getUsername(), info.getPassword());
        if (authData == null){
            response.status(401);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: unauthorized");
            return new Gson().toJson(ec);
        }
        else{
            return new Gson().toJson(authData);
        }
    }

    private Object LogoutHandler(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("authorization");
        String result = userService.logout(authToken);
        if (result == null){
            response.status(401);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: unauthorized");
            return new Gson().toJson(ec);
        }
        return new Gson().toJson(result);
    }

    private Object ListGamesHandler(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("authorization");
        Collection<GameData> list = gameService.ListGames(authToken);
        if (list == null){
            response.status(401);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: unauthorized");
            return new Gson().toJson(ec);
        }
        else{
            return new Gson().toJson(list);
        }
    }

    private Object CreateGameHandler(Request request, Response response) throws DataAccessException {
        var serializer = new Gson();
        String authToken = request.headers("authorization");
        var info = serializer.fromJson(request.body(), GameData.class);
        int gameID = gameService.createGame(authToken, info.getGameName());
        if (gameID == 0){
            response.status(401);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: unauthorized");
            return new Gson().toJson(ec);
        }
        else if (gameID == -1){
            response.status(500);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: name already taken");
            return new Gson().toJson(ec);
        }
        else{
            CreateGameResult cgr = new CreateGameResult(gameID);
            return new Gson().toJson(cgr);
        }
    }

    private Object JoinGameHandler(Request request, Response response) throws DataAccessException {
        var serializer = new Gson();
        String authToken = request.headers("authorization");
        var info = serializer.fromJson(request.body(), JoinRequest.class);
        String result = gameService.joinGame(authToken, info.getColor(), info.getGameID());
        if (result == null){
            response.status(401);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: unauthorized");
            return new Gson().toJson(ec);
        }
        return new Gson().toJson(result);
    }
}
