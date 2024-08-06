package client;

import chess.ChessGame;
import dataaccess.SQLAuthDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;
import ui.ListGamesResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() throws URISyntaxException, IOException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    void runBefore() throws URISyntaxException, IOException {
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
    void login() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        AuthData loginResult = facade.login("player1", "password");
        Assertions.assertEquals("player1", loginResult.getUsername());
    }

    @Test
    void logout() throws Exception {
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        facade.logout(authData.getAuthToken());
        SQLAuthDAO sad = new SQLAuthDAO();
        HashMap<String, AuthData> list = sad.getAuths();
        Assertions.assertNull(list.get("player1"));
    }

    @Test
    void createGame() throws Exception {
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        int gameID = facade.createGame(authData.getAuthToken(), "newGame");
        Assertions.assertEquals(1, gameID);
    }

    @Test
    void listGames() throws Exception {
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        facade.createGame(authData.getAuthToken(), "newGame");
        ListGamesResponse list = facade.listGames(authData.getAuthToken());
        Collection<GameData> games = list.getGameDataList();
        Assertions.assertNotNull(games);
    }

    @Test
    void joinGame() throws Exception {
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        int gameID = facade.createGame(authData.getAuthToken(), "newGame");
        facade.joinGame(authData.getAuthToken(), ChessGame.TeamColor.WHITE, gameID);
        ListGamesResponse list = facade.listGames(authData.getAuthToken());
        Collection<GameData> games = list.getGameDataList();
        for(GameData game : games){
            Assertions.assertEquals("player1", game.getWhiteUsername());
        }
    }
}
