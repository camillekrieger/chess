package ui;

import client.ChessClient;

import java.util.Scanner;

public class Repl {
    private final ChessClient client;

    public Repl(int serverURL){
        client = new ChessClient(serverURL);
    }

    public void run(){
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type Help to get started. \uD83D\uDC36");
        System.out.println();
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String input = scanner.nextLine();
            try {
                result = client.eval(input);
                System.out.print(result);
                if (result.equals("You are logged out")){
                    break;
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\nprompt: ");
    }
}
