package ui;

import client.ChessClient;

import java.util.Scanner;

public class Repl {
    private final ChessClient client;
    private final State state;

    public Repl(int serverURL){
        state = State.LOGGED_OUT;
        client = new ChessClient(serverURL, state);
    }

    public void run(){
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type help to get started. \uD83D\uDC36");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String input = scanner.nextLine();
            try {
                result = client.eval(input);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.printf("\n[%s] >>> ", state);
    }
}
