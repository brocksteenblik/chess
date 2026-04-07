package handler.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        var sessionList = connections.get(gameID);
        if (sessionList == null){
            connections.put(gameID, new ArrayList<>(List.of(session)));
        }
        else {
            sessionList.add(session);
            connections.put(gameID, sessionList);
        }
    }

    public void remove(Session session, int gameID) {
        var sessionList = connections.get(gameID);
        sessionList.remove(session);
        connections.put(gameID, sessionList);
    }

    public void broadcast(Session excludeSession, int gameID, ServerMessage message) throws IOException {
        String msg = message.toString();
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void messageRoot(Session session, int gameID, ServerMessage message) throws IOException{
        String msg = message.toString();
        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        }
    }
}
