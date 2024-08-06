package ui;

public class PostloginUI {

    public PostloginUI(){}

    public String help(){
        return """
                  \tcreate <NAME> - a game
                  \tlist - games
                  \tjoin <ID> [WHITE|BLACK] - a game
                  \tobserve <ID> - a game
                  \tlogout - when you are done
                  \tquit - playing chess
                  \thelp - with possible commands""";
    }
}
