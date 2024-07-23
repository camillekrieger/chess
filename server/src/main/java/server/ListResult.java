package server;

public class ListResult {
    int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;

    public ListResult(int id, String wUser, String bUser, String gName){
        this.gameID = id;
        this.whiteUsername = wUser;
        this.blackUsername = bUser;
        this.gameName = gName;
    }
}
