package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

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
//        Spark.delete("/session", this::LogoutHandler);
//        Spark.get("/game", this::ListGamesHandler);
//        Spark.post("/game", this::CreateGameHandler);
//        Spark.put("/game", this::JoinGameHandler);
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
        clearService.clear();
        return new Gson().toJson(response);
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
        String username = info.getUsername();
        String password = info.getPassword();
        AuthData authData = userService.login(username, password);
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

//    private Object LogoutHandler(Request request, Response response) {
//        var serializer = new Gson();
//        String authToken = request.headers("authorization");
//        return null;
//    }
}
