package Game.Controller;

import Game.Model.GameWorld;
import Game.Model.PlaneMaze;
import Game.Model.iWorld;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;
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

        JsonParser parser = Json.createParser(new StringReader(msg));

        String cmd = getCommand(parser);
        String playerName = getPlayerName(parser);


        doCommand(playerName, cmd, session, parser);
        //save connection to this player
    }

    private static String getCommand(JsonParser parser) {
        parser.next();
        parser.next();
        parser.next();
        return parser.getString();
    }

    private static String getPlayerName(JsonParser parser) {
        parser.next();
        parser.next();
        return parser.getString();
    }

    private static int getX(JsonParser parser) {
        parser.next();
        parser.next();
        parser.next();
        parser.next();
        return parser.getInt();
    }

    private static int getY(JsonParser parser) {
        parser.next();
        parser.next();
        return parser.getInt();
    }

    private void doCommand(String playerName, String cmd, Session session, JsonParser parser) {
        System.out.println("CMD is : " + cmd);
        switch (cmd) {
            case "init":
                initialConnection(playerName, session);
                break;
            case "update":
                int x = getX(parser);
                int y = getY(parser);
                rooms.get(playerName).setPlayerCoordinates(playerName, x, y);
                System.out.println("player sent update");
                break;
        }
    }

    private void initialConnection(String playerName, Session session) {
        connections.put(playerName, session);

        if (rooms.get(playerName) == null) {
            iWorld world = new GameWorld(new PlaneMaze(20, 20));
            world.addPlayer(playerName);
            roomMates.get(playerName).forEach(x-> rooms.put(x, world));
        } else {
            iWorld world = rooms.get(playerName);
            world.addPlayer(playerName);

            if (world.getPlayers().size() == roomMates.get(playerName).size()) {
                System.out.println("Room is Full");
                addSchedule(world);
                world.startGame();
            }
        }
        try {
            session.getBasicRemote().sendText(rooms.get(playerName).getInit().toString());
        } catch (IOException e) {
            System.out.println("COULD NOT SEND INIT TO " + playerName);
            //e.printStackTrace();
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
                    connections.get(x).getBasicRemote().sendText(world.getUpdate(x).toString());
                } catch (IOException e) {
                    System.out.println("could not send gameState to " + x);
                    //e.printStackTrace();
                }
            });
            System.out.println("sent ");
        }, 100, 5000, TimeUnit.MILLISECONDS);

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
