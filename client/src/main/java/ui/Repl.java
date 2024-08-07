package ui;

import client.ChessClient;

import java.io.IOException;
import java.util.Scanner;

public class Repl {
    private final ChessClient client;
    private State state;

    public Repl(int serverURL) throws IOException {
        state = State.LOGGED_OUT;
        client = new ChessClient(serverURL, state);
    }

    public void run(){
        System.out.println("\uD83D\uDC51 Welcome to 240 chess. Type help to get started. \uD83D\uDC51");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            result = scanner.nextLine();
            try {
                String output = client.eval(result);
                System.out.print(output);
                changeState(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void changeState(String result){
        if (result.contains("register") || result.contains("login") || result.contains("exit")){
            state = State.LOGGED_IN;
        }
        else if (result.contains("logout")){
            state = State.LOGGED_OUT;
        }
        else if (result.contains("join") || result.contains("observe")){
            state = State.PLAYGAME;
        }
    }

    private void printPrompt() {
        System.out.printf("\n[%s] >>> ", state);
    }
}
