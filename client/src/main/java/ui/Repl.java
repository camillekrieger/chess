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
        System.out.print("[LOGGED_OUT] >>> ");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.print(client.help());


        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(result);
                if (result.equals("logout")){
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
        System.out.print("prompt");
    }
}
