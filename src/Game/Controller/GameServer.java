package Game.Controller;

import Core.Controller.RatingManager;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/gameServer", configurator=ServerConfig.class)
public class GameServer {
    public static final int WORKING_THREAD_NUMBER = 10;
    private GameManager gameManager;
    private String playerName;
    /**
     * default constructor
     */
    public GameServer() {

    }

    @OnOpen
    public void open(Session session, EndpointConfig config) {
        //read name of a user from its httpSession
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String userName = (String) httpSession.getAttribute("nickname");
        playerName = userName;

        //initialize gameManager
        initGameManager(httpSession);
        System.out.println("connected: " + playerName + " Session:" + session + " http: " + httpSession);
    }

    /**
     * initializing gameManager
     * read information about players distribution in
     * playing rooms
     * @param httpSession is needed to get ServletContext
     */
    private void initGameManager(HttpSession httpSession) {
        gameManager = (GameManager) httpSession.getServletContext().getAttribute(GameManager.class.getName());
        if (gameManager == null) throw new RuntimeException("COULD NOT GET GAME MANAGER");
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

        //reading playerName from msg is not arbitrary
        //but is still necessary from parser
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
        System.out.println("connection closed with " + playerName);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.out.println("OnError");
        //t.printStackTrace();
    }
}
