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
        World world = rooms.get(playerName);

        switch (cmd) {
            case "init": {
                connections.put(playerName, session);
                if (rooms.get(playerName) == null) {
                    //TODO create new world object
                    for (String s : roomMates.get(playerName)) {
                        rooms.put(s, null); //here will be world object instead of null
                    }
                    //world.addPlayer(playerName);
                } else {
                    //put the playerName in rooms.get(playerName)
                    //if there are as many players in the world as
                    //in roommates.get(playerName).size() then
                    //world.gameStarted();
                    //sendMazeToAllPlayers(world);
                    //sendStateToAllPlayers(world);
                }
            }
            case "move": {
                //here will be dx, dy from player message instead of
                //2 and 3
                world.playerMove(playerName, 2, 3);
                sendWorldStateToUsers(world);
            }
        }
    }

    private void analizeMessage(String smg, String cmd, String playerName, String move) {

    }

    private void sendWorldStateToUsers(World world) {
        String toSend = world.getState();
        world.getPlayers().forEach(x -> {
            try {
                connections.get(x).getBasicRemote().sendText(toSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendWorldMazeToUsers(World world) {
        String toSend = world.getMaze();
        world.getPlayers().forEach(x -> {
            try {
                connections.get(x).getBasicRemote().sendText(toSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
