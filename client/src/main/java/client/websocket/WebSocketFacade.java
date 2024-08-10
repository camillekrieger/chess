package client.websocket;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;

public class WebSocketFacade extends Endpoint implements MessageHandler {
    private Session session;

    //gameHandler?

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        //do stuff
    }
    @Override
    public void onClose(){
        //do stuff
    }

    @Override
    public void onError(){
        //do stuff
    }

    public void connect(){
        //do stuff
    }

    public void makeMove(){
        //do stuff
    }

    public void leaveGame(){
        //leave the game
    }

    public void resignGame(){
        //yeah do that
    }

    public void onMessage(String message){
        //deserialize method
        //call GameHAndler to process message
    }

    private void sendMessage(){
        //create command message
        //send message to server
    }
}
