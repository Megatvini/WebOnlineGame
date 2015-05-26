package Game.Controller;

import Game.Model.World;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

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
        if (roomMates == null) RoomMateMap(config);
    }

    private void RoomMateMap(EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
        roomMates = (Map<String, List<String>>) httpSession.getServletContext().getAttribute("roomates");
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        String cmd = "";
        String playerName = "";
        String move = "";
        analizeMessage(msg, cmd, playerName, move);

        switch (cmd) {
            case "initialization": {
                connections.put(playerName, session);
                if (rooms.get(playerName) == null) {
                    //TODO create new world object
                    for (String s : roomMates.get(playerName)) {
                        rooms.put(s, null); //here will be world object instead of null
                    }
                    //world.addPlayer(playerName);
                } else {
                    //put the playerName in rooms.get(playerName)
                }
                //send the maze to the playerName via session;
            }
            case "move": {

            }
        }
    }

    private void analizeMessage(String smg, String cmd, String playerName, String move) {

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
}
