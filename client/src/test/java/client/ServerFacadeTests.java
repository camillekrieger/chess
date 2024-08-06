package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() throws URISyntaxException, IOException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
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
}
