package client;

import chess.ChessGame;
import dataaccess.SQLAuthDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;

import java.io.IOException;
import java.util.HashMap;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    void runBefore() throws IOException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void register() throws Exception {
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(authData.getAuthToken().length() > 10);
    }

    @Test
    void registerFail() throws IOException {
        Assertions.assertThrows(IOException.class, () -> {
            facade.register("player1", "password", "p1@email.com");
            facade.register("player1", "newPassword", "player@email.com");
        });
    }

    @Test
    void login() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        AuthData loginResult = facade.login("player1", "password");
        Assertions.assertEquals("player1", loginResult.getUsername());
    }

    @Test
    void loginFail() {
        Assertions.assertThrows(IOException.class, () -> {
            facade.register("player1", "password", "p1@email.com");
            facade.login("player1", "wrongPassword");
        });
    }

    @Test
    void logout() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.login("player1", "password");
        facade.logout();
        SQLAuthDAO sad = new SQLAuthDAO();
        HashMap<String, AuthData> list = sad.getAuths();
        Assertions.assertNull(list.get("player1"));
    }

    @Test
    void createGame() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        int gameID = facade.createGame("newGame");
        Assertions.assertEquals(1, gameID);
    }

    @Test
    void createGameFail() {
        Assertions.assertThrows(IOException.class, () -> {
            facade.register("player1", "password", "p1@email.com");
            facade.createGame("newGame");
            facade.createGame("newGame");
        });
    }

    @Test
    void listGames() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("newGame");
        GameData[] list = facade.listGames();
        Assertions.assertNotNull(list);
    }

    @Test
    void joinGame() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        int gameID = facade.createGame("newGame");
        facade.joinGame(ChessGame.TeamColor.WHITE, gameID);
        GameData[] games = facade.listGames();
        for (GameData game : games){
            if (game.getGameID() == gameID) {
                Assertions.assertEquals("player1", game.getWhiteUsername());
            }
        }
    }

    @Test
    void joinGameFail(){
        Assertions.assertThrows(IOException.class, () -> {
            facade.register("player1", "password", "p1@email.com");
            int gameID = facade.createGame("newGame");
            facade.joinGame(ChessGame.TeamColor.BLACK, gameID);
            facade.register("piglet", "wind", "pig@hawoods.com");
            facade.joinGame(ChessGame.TeamColor.BLACK, gameID);
        });
    }

    @Test
    void clear() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("newGame");
        facade.clear();
    }
}
