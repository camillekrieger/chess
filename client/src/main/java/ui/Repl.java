package ui;

import chess.ChessGame;
import client.ChessClient;
import client.websocket.NotificationHandler;
import java.util.Scanner;

public class Repl implements NotificationHandler {
    private final ChessClient client;
    private State state;

    public Repl(int serverURL) {
        state = State.LOGGED_OUT;
        client = new ChessClient(serverURL, state, this);
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
                if (output != null){
                    System.out.print(output);
                    changeState(result);
                }
                else{
                    System.out.print("Invalid input. Please try again.");
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void changeState(String result){
        String[] words = result.split("\\s+");
        int size = words.length;
        if (result.contains("register") && size == 4) {
            state = State.LOGGED_IN;
        }
        else if (result.contains("login") && size == 3) {
            state = State.LOGGED_IN;
        }
        else if (result.contains("leave")){
            state = State.LOGGED_IN;
        }
        else if (result.contains("logout")){
            state = State.LOGGED_OUT;
        }
        else if (result.contains("join") && size == 3){
            state = State.PLAYGAME;
        }
        else if (result.contains("observe") && size == 2){
            state = State.PLAYGAME;
        }
    }

    private void printPrompt() {
        System.out.printf("\n[%s] >>> ", state);
    }

    @Override
    public void updateGame(ChessGame game) {
        client.setCurrGame(game);
        client.redrawBoard();
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }
}
