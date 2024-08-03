package ui;

import client.ChessClient;

public class Repl {
    private final ChessClient client;

    public Repl(String serverURL){
        client = new ChessClient();
    }

    public void run(){
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type Help to get started. \uD83D\uDC36");
        System.out.print(client.help());
    }
}
