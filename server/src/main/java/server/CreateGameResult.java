package server;

public class CreateGameResult {
    int gameID;

    public CreateGameResult(int id){
        this.gameID = id;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
