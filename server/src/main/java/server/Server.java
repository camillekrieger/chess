package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.sql.SQLException;
import java.util.Collection;

public class Server {

    UserService userService = new UserService();
    GameService gameService = new GameService();

    ClearService clearService = new ClearService();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearHandler);
        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGamesHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);
        Spark.delete("game", this::leaveGameHandler);
        Spark.get("/game", this::getGameHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clearHandler(Request request, Response response) throws DataAccessException, SQLException {
        clearService.clear();
        JsonObject emptyJsonObject = new JsonObject();
        return new Gson().toJson(emptyJsonObject);
    }

    private Object registerHandler(Request request, Response response) throws DataAccessException, SQLException {
        var serializer = new Gson();
        var info = serializer.fromJson(request.body(), UserData.class);
        AuthData authData = userService.register(info);
        if (authData == null){
            response.status(400);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: bad request");
            return new Gson().toJson(ec);
        }
        else if ("taken".equals(authData.getUsername())){
            response.status(403);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: already taken");
            return new Gson().toJson(ec);
        }
        else {
            return new Gson().toJson(authData);
        }
    }

    private Object loginHandler(Request request, Response response) throws DataAccessException, SQLException {
        var serializer = new Gson();
        var info = serializer.fromJson(request.body(), UserData.class);
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

    private Object logoutHandler(Request request, Response response) throws DataAccessException, SQLException {
        String authToken = request.headers("authorization");
        String result = userService.logout(authToken);
        if (result == null){
            response.status(401);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: unauthorized");
            return new Gson().toJson(ec);
        }
        JsonObject emptyJsonObject = new JsonObject();
        return new Gson().toJson(emptyJsonObject);
    }

    private Object listGamesHandler(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("authorization");
        Collection<GameData> list = gameService.listGames(authToken);
        if (list == null){
            response.status(401);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: unauthorized");
            return new Gson().toJson(ec);
        }
        else{
            ListGamesResult lgr = new ListGamesResult();
            lgr.addGames(list);
            return new Gson().toJson(lgr);
        }
    }

    private Object createGameHandler(Request request, Response response) throws DataAccessException, SQLException {
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

    private Object joinGameHandler(Request request, Response response) throws DataAccessException {
        var serializer = new Gson();
        String authToken = request.headers("authorization");
        JoinRequest info = serializer.fromJson(request.body(), JoinRequest.class);
        if (info.getGameID() == null || info.getColor() == null || info.getGameID() < 1){
            response.status(400);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: bad request");
            return new Gson().toJson(ec);
        }
        String result = gameService.joinGame(authToken, info.getColor(), info.getGameID());
        if (result == null){
            response.status(401);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: unauthorized");
            return new Gson().toJson(ec);
        }
        else if (result.equals("no color")){
            response.status(400);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: bad request");
            return new Gson().toJson(ec);
        }
        else if(result.equals("taken")){
            response.status(403);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: already taken");
            return new Gson().toJson(ec);
        }
        JsonObject emptyJsonObject = new JsonObject();
        return new Gson().toJson(emptyJsonObject);
    }

    private Object leaveGameHandler(Request request, Response response) throws DataAccessException {
        var serializer = new Gson();
        String authToken = request.headers("authorization");
        JoinRequest info = serializer.fromJson(request.body(), JoinRequest.class);
        if (info.getGameID() == null || info.getColor() == null || info.getGameID() < 1){
            response.status(400);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: bad request");
            return new Gson().toJson(ec);
        }
        String result = gameService.leaveGame(authToken, info.getColor(), info.getGameID());
        if (result == null){
            response.status(401);
            ErrorClass ec = new ErrorClass();
            ec.setMessage("Error: Not able to perform request");
            return new Gson().toJson(ec);
        }
        JsonObject emptyJsonObject = new JsonObject();
        return new Gson().toJson(emptyJsonObject);
    }

    private Object getGameHandler(Request request, Response response) throws DataAccessException {
        var serializer = new Gson();
        CreateGameResult info = serializer.fromJson(request.body(), CreateGameResult.class);
        return gameService.getGame(info.getGameID());
    }
}
