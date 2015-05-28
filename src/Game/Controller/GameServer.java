package Game.Controller;

import Game.Model.World;
import Game.Model.WorldMock;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@ServerEndpoint(value="/game", configurator=ServerConfig.class)
public class GameServer {
    private Map<String, List<String>> roomMates;
    private Map<String, World> rooms;
    private Map<String, Session> connections;

    public GameServer() {
        rooms = Collections.synchronizedMap(new HashMap<>());
        connections = Collections.synchronizedMap(new HashMap<>());
    }


    @OnOpen
    public void open(Session session, EndpointConfig config) {
        System.out.println("someone connected");
        if (roomMates == null) RoomMateMap(config);
    }

    private void RoomMateMap(EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
        roomMates = (Map<String, List<String>>) httpSession.getServletContext().getAttribute("roomMates");
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        System.out.println("message received: " + msg);
        String playerName = msg.split(":")[0];
        //save connection to this player
        connections.put(playerName, session);

        if (rooms.get(playerName) == null) {
            World world = new WorldMock();
            world.addPlayer(playerName);
            roomMates.get(playerName).forEach(x-> {
                rooms.put(x, world);
            });
            //add scheduled executor
        } else {
            rooms.get(playerName).addPlayer(playerName);
        }
    }


    @OnClose
    public void onClose(Session session) {
        System.out.println("connection closed");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.out.println("OnError");
        //t.printStackTrace();
    }

    private class Worker extends Thread {
        public Worker(World world) {

        }

    }
}
