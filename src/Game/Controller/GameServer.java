package Game.Controller;

import Game.Model.GameWorld;
import Game.Model.PlaneMaze;
import Game.Model.iWorld;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@ServerEndpoint(value="/game", configurator=ServerConfig.class)
public class GameServer {
    private Map<String, List<String>> roomMates;
    private Map<String, iWorld> rooms;
    private Map<String, Session> connections;
    private ScheduledThreadPoolExecutor executor;

    public GameServer() {
        rooms = Collections.synchronizedMap(new HashMap<>());
        connections = Collections.synchronizedMap(new HashMap<>());
        executor = new ScheduledThreadPoolExecutor(10);
    }

    @OnOpen
    public void open(Session session, EndpointConfig config) {
        System.out.println("someone connected");
        try {
            session.getBasicRemote().sendText("hello");
        } catch (IOException e) {
            System.out.println("could not send hello onOpen");
            e.printStackTrace();
        }
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
        String cmd = msg.split(":")[1];

        doCommand(playerName, cmd, session);
        //save connection to this player
    }

    private void doCommand(String playerName, String cmd, Session session) {
        System.out.println("CMD is : " + cmd);
        switch (cmd) {
            case "init":
                initialConnection(playerName, session);
                break;
            case "update":
                rooms.get(playerName).setPlayerCoordinates(playerName, 1, 2);
                System.out.println("player sent update");
                break;
        }
    }

    private void initialConnection(String playerName, Session session) {
        connections.put(playerName, session);

        if (rooms.get(playerName) == null) {
            iWorld world = new GameWorld(new PlaneMaze());
            world.addPlayer(playerName);
            roomMates.get(playerName).forEach(x-> rooms.put(x, world));
        } else {
            iWorld world = rooms.get(playerName);
            world.addPlayer(playerName);
            if (world.getPlayers().size() == roomMates.get(playerName).size()) {
                //add scheduled executor
                System.out.println("Room is Full");
                addSchedule(world);
            }
        }
    }

    private void addSchedule(iWorld world) {
        executor.scheduleAtFixedRate(() -> {
            if (!world.gameOn()) {
                System.out.println("GAMEISOVER");
                world.getPlayers().forEach(x->{
                    roomMates.remove(x);
                    rooms.remove(x);
                });
                throw new RuntimeException("Game is Over");
            }

            world.getPlayers().forEach(x -> {
                try {
                    //asd == world.getState();
                    connections.get(x).getBasicRemote().sendText("asd");
                } catch (IOException e) {
                    System.out.println("could not send gameState to " + x);
                    //e.printStackTrace();
                }
            });
        }, 100, 500, TimeUnit.MILLISECONDS);

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
