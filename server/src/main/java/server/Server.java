package server;

import service.AuthService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        record LoginRequest(String username, String password){}

        record RegisterRequest(String Username, String Password, String Email){}

        record LogoutRequest(String authToken){}

        record ListGames(String authToken){}

        record CreateGame(String authToken, String gameName){}
        record JoinGame(String playerColor, int gameID){}
        record clear(){}
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
