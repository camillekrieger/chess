package ui;

public class PostloginUI {

    public PostloginUI(){}

    public String help(){
        return """
                - create <NAME> - a game
                - list - games
                - join <ID> [WHITE|BLACK] - a game
                - observe <ID> - a game
                - logout - when you are done
                - quit - playing chess
                - help - with possible commands""";
    }
}
