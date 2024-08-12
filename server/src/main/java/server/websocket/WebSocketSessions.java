package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebSocketSessions {
    private Map<Integer, Set<Session>> sessions = new HashMap<>();

    public void addSessionToGame(int gameID, Session session){
        sessions.computeIfAbsent(gameID, k -> new HashSet<>());
        sessions.get(gameID).add(session);
    }

    public void removeSessionFromGame(int gameID, Session session){
        sessions.get(gameID).remove(session);
    }

    public void removeSession(Session session){
        for(int gameID : sessions.keySet()){
            sessions.get(gameID).remove(session);
        }
    }

    public Set<Session> getSessionsFromGame(int gameID){
        return sessions.get(gameID);
    }
}
