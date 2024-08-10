package ui;

public class GetGameRequest {

    private int gameID;

    public GetGameRequest(int gameID){
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
