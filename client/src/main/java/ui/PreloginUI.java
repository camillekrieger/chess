package ui;

public class PreloginUI {

    public PreloginUI(){}

    public String help(){
        return """
                  \tregister <USERNAME> <PASSWORD> <EMAIL> - to create an account
                  \tlogin <USERNAME> <PASSWORD> - to play chess
                  \tquit - playing chess
                  \thelp - with possible commands""";
    }
}
