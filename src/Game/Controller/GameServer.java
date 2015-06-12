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

    /**
     * constructor only used for testing purposes
     * @param manager
     */
    public GameServer(GameManager manager) {
        gameManager = manager;
        userConnectionMap = new ConcurrentHashMap<>();
    }

    public GameServer() {
        userConnectionMap = new ConcurrentHashMap<>();
    }


    @OnOpen
    public void open(Session session, EndpointConfig config) {
        //read name of a user from its httpSession
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
        String userName = (String) httpSession.getAttribute("userName");
        userConnectionMap.put(session, userName);

        //initialize gameManager only once
        if (gameManager == null) RoomMateMap(httpSession);
       // System.out.println("someone connected " + session);
    }

    /**
     * initializing gameManager
     * read information about players distribution in
     * playing rooms
     * @param httpSession is needed to get ServletContext
     */
    private synchronized void RoomMateMap(HttpSession httpSession) {
        Map<String, Collection<String>> roomMates = (Map<String, Collection<String>>)
                httpSession.getServletContext().getAttribute("roomMates");
        gameManager = new GameManager(roomMates, new GameFactory(), new UserConnector(),
                new ScheduledThreadPoolExecutor(WORKING_THREAD_NUMBER));
        if (roomMates == null) throw new RuntimeException("COULD NOT GET ROOMMATES");
    }

    /**
     * gets called when user sends a message
     * @param msg message of a user
     * @param session session of a user
     */
    @OnMessage
    public void onMessage(String msg, Session session) {
        //System.out.println("message received: " + msg + this);
        PlayerJsonParser parser = new PlayerJsonParser(msg);
        String cmd = parser.getCommand();
        String playerName = userConnectionMap.get(session);

        //reading playerName from msg is not arbitrary
        //but is still neccessary from parser
        parser.getPlayerName();
        int x = parser.getXCoord();
        int y = parser.getYCoord();
        doCommand(playerName, cmd, session, x, y);
    }

    /**
     * execute the command sent by a player
     * @param playerName name of a player
     * @param cmd command type like "init" or "update"
     * @param session session of a player
     * @param x players current x coordinate
     * @param y players current y coordinate
     * x and y are only used if cmd is "update"
     */
    private void doCommand(String playerName, String cmd, Session session, int x, int y) {
        //System.out.println("CMD is : " + cmd+".");
        //System.out.println("PLAYER : " + playerName+".");
        switch (cmd) {
            case "init":
                gameManager.addPlayer(playerName, session);
                break;
            case "update":
                gameManager.setUpdateFromPlayer(playerName, x, y);
               // System.out.println("player sent update");
                break;
        }
    }


    /**
     * gets called when connection gets closed;
     * @param session session of a users
     */
    @OnClose
    public void onClose(Session session) {
        String playerName = userConnectionMap.get(session);
        if (playerName != null)
            gameManager.removePlayer(userConnectionMap.get(session));
        userConnectionMap.remove(session);
        System.out.println("connection closed with " + playerName);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.out.println("OnError");
        t.printStackTrace();
    }
}
