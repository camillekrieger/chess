package client.websocket;

import ui.Repl;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;

public class WebSocketFacade extends Endpoint implements MessageHandler {
    private Session session;
    private Repl repl;

    //gameHandler?

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

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
        //call Repl to process message
        //needs a switch to know what message you're getting
    }

    private void sendMessage(){
        //create command message
        //send message to server
    }
}
