package ui;

public class PreloginUI {

    public PreloginUI(){}

    public String help(){
        return """
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                - login <USERNAME> <PASSWORD> - to play chess
                - quit - playing chess
                - help - with possible commands""";
    }
}
