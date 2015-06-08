package Game.Controller;


import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.*;

@ServerEndpoint(value="/game", configurator=ServerConfig.class)
public class GameServer {
    private GameManager gameManager;
    private ConcurrentMap<Session, String> userConnectionMap;
    private static final int WORKING_THREAD_NUMBER = 10;

    public GameServer(GameManager manager) {
        gameManager = manager;
        userConnectionMap = new ConcurrentHashMap<>();
    }

    public GameServer() {
        userConnectionMap = new ConcurrentHashMap<>();
    }


    @OnOpen
    public void open(Session session, EndpointConfig config) {
        if (gameManager == null) RoomMateMap(config);
       // System.out.println("someone connected " + session);
    }

    private synchronized void RoomMateMap(EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
        Map<String, Collection<String>> roomMates = (Map<String, Collection<String>>)
                httpSession.getServletContext().getAttribute("roomMates");
        gameManager = new GameManager(roomMates, new GameFactory(), new UserConnector(),
                new ScheduledThreadPoolExecutor(WORKING_THREAD_NUMBER));
        if (roomMates == null) throw new RuntimeException("COULD NOT GET ROOMMATES");
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        //System.out.println("message received: " + msg + this);
        PlayerJsonParser parser = new PlayerJsonParser(msg);
        String cmd = parser.getCommand();
        String playerName = parser.getPlayerName();
        int x = parser.getXCoord();
        int y = parser.getYCoord();
        doCommand(playerName, cmd, session, x, y);
    }

    private void doCommand(String playerName, String cmd, Session session, int x, int y) {
       // System.out.println("CMD is : " + cmd+".");
        //System.out.println("PLAYER : " + playerName+".");
        userConnectionMap.put(session, playerName);
        switch (cmd) {
            case "init":
                gameManager.addPlayer(playerName, session.getBasicRemote());
                break;
            case "update":
                gameManager.setUpdateFromPlayer(playerName, x, y);
               // System.out.println("player sent update");
                break;
        }
    }


    @OnClose
    public void onClose(Session session) {
       System.out.println("connection closed");
        gameManager.removePlayer(userConnectionMap.get(session));
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.out.println("OnError");
        //t.printStackTrace();
    }
}
