package Game.Controller;

import Core.Controller.RatingManager;
import Game.Model.iWorld;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


public class GameManager {
    private static final int INITIAL_UPDATE_DELAY = 5000;
    private static final int UPDATE_INTERVAL = 40;
    private static final int SERVICE_CANCEL_INTERVAL = 500;

    // roomMates map stores information about players who are in the same room
    //new Players are added by MatchMaker Servlet
    private Map<String, Collection<String>> roomMates;

    //factory class used to create iWorld objects
    private GameFactory gameFactory;

    //connector object which takes care of sending messages to users;
    private UserConnector connector;

    //connects players to the game worlds
    private Map<String, iWorld> rooms;

    //executes scheduled tasks concurrently
    private ScheduledThreadPoolExecutor executor;

    //running services of scheduledThreadPoolExecutor
    private Map<iWorld, ScheduledFuture> runningServices;

    //change rating of players after game
    private RatingManager ratingManager;

    /**
     *
     * @param roomMates used to distribute connected players in rooms
     * @param factory used to create new GameWorld objects
     * @param connector object for communicating with users
     * @param executor Thread Pool for completing scheduled tasks
     */
    public GameManager(Map<String, Collection<String>> roomMates, GameFactory factory,
                       UserConnector connector, ScheduledThreadPoolExecutor executor,
                       RatingManager ratingManager) {
        this.roomMates = roomMates;
        this.gameFactory = factory;
        this.connector = connector;
        this.executor = executor;
        this.rooms = Collections.synchronizedMap(new HashMap<>());
        this.runningServices = new ConcurrentHashMap<>();
        this.ratingManager = ratingManager;

        scheduleCleaningService();
    }

    /**
     * schedules cleaning service, which periodically checks if any of
     * running services should be canceled and cancels them if necessary
     */
    private void scheduleCleaningService() {
        executor.scheduleAtFixedRate(() -> runningServices.keySet().forEach(world -> {
            if (world.isFinished()) {
                System.out.println("GAME IS OVER");
                runningServices.remove(world).cancel(true);
                world.getPlayers().forEach(x->{
                    roomMates.remove(x);
                    rooms.remove(x);
                    connector.removeUser(x);
                });
                ratingManager.addNewGameResults(world.playerPlaces());
            }
        }), SERVICE_CANCEL_INTERVAL, SERVICE_CANCEL_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * adds new player to the game
     * Manager puts the player in the room together with roomMates defined
     *  by roomMates structure
     * @param playerName name of a player
     * @param playerConnection connection used to communicate with player
     */
    public synchronized void addPlayer(String playerName, Session playerConnection) {
        connector.addUser(playerName, playerConnection);
        if (rooms.get(playerName) == null) {
            iWorld world = gameFactory.getNewInstance();
            world.addPlayerAtCorner(playerName);
            Collection<String> mates = roomMates.get(playerName);
            mates.forEach(player->rooms.put(player, world));
        } else {
            iWorld world = rooms.get(playerName);
            world.addPlayerAtCorner(playerName);
        }
        System.out.println(roomMates);
        checkIfRoomIsFull(playerName);
    }


    /**
     * send initial information to player
     * it is called only once for each connected players
     * @param playerName name of a player
     */
    private void sendInit(String playerName) {
        String toSend = rooms.get(playerName).getInit().toString();
        System.out.println("SENDING " + toSend);
        connector.sendMessageTo(playerName, toSend);
    }

    /**
     * checks if room of playerName is full
     * and if so starts the game
     * @param playerName name of a player
     */
    private void checkIfRoomIsFull(String playerName) {
        iWorld world = rooms.get(playerName);
        Collection <String> players = world.getPlayers();
        System.out.println("PLayers size " + players.size());
        if (players.size() == roomMates.get(playerName).size()) {
            System.out.println("Room is Full");
            players.forEach(this::sendInit);
            scheduleUpdate(world);
            world.startGame();
        }
    }

    /**
     * schedule new task to serve the players of a world
     * @param world the world task is serving,
     * meaning sending updates to players periodically
     */
    private void scheduleUpdate(iWorld world) {
        ScheduledFuture scheduledFuture = executor.scheduleAtFixedRate(() -> {
            world.getPlayers().forEach(x -> connector.sendMessageTo(x, world.getUpdate(x).toString()));
           // System.out.println("sent update");
        }, INITIAL_UPDATE_DELAY, UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
        if (scheduledFuture != null) runningServices.put(world, scheduledFuture);
    }

    /**
     * removes player that has disconnected from game
     * @param playerName name of a player
     */
    public void removePlayer(String playerName) {
        connector.removeUser(playerName);
    }

    /**
     * this method should be called when server receives update from a player
     * @param playerName name of a player
     * @param xCoord x coordinate of a player
     * @param yCoord y coordinate of a player
     * @return returns true iff information passed is valid
     */
    public synchronized boolean setUpdateFromPlayer(String playerName, int xCoord, int yCoord) {
        iWorld world = rooms.get(playerName);
        if (world == null) return false;
        world.setPlayerCoordinates(playerName, xCoord, yCoord);
        return true;
    }
}
