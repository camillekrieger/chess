package ui;

public class CreateGameResponse {

    private int gameID;
    private String message;

    public CreateGameResponse(int gameID, String message){
        this.gameID = gameID;
        this.message = message;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
