package websocket.messages;

public class ErrorMessage extends ServerMessage{
    String errorMessage = "Error: ";

    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage += errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
