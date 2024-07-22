package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.ClearService;
import service.UserService;
import spark.*;

public class Server {

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
        ClearService clearService = new ClearService();
        clearService.clear();
        return new Gson().toJson(response);
    }

    private Object RegisterHandler(Request request, Response response) throws DataAccessException {
        var serializer = new Gson();
        var info = serializer.fromJson(request.body(), UserData.class);
        UserService userService = new UserService();
        AuthData authData = userService.register(info);
        return new Gson().toJson(authData);
    }
}
