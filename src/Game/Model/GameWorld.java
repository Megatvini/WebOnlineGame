package Game.Model;

import javax.json.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * Created by SHAKO on 30-May-15.
 * GameWorld class, for creating and interacting with game,
 * changing and getting state of it.
 */
public class GameWorld implements iWorld {

    // enum of states of this class instance
    enum State {NEW, RUNNING, FINISHED}

    // configuration variables
    private int maxPlayers;
    private int startLifeNum;
    private double plusDist;
    private long plusDistDelay;
    private int startPotNum;
    private long addPotDelay;
    private int potForKick;

    // state of this class
    private State state;

    // maze object which has raw info about maze for this game world
    private GameMaze gm;

    // fill depended on info passed in constructor
    private Map<String, Player> nameOnPlayer;

    // new potions to send by json
    private Map<String, Set<Point2D.Double>> potsToAdd;

    // potions to remove from display, send as json
    private Map<String, List<Integer>> potsToRemove;

    // getting value according to number of active players
    private int activePlNum;

    // when game starts, starts two task in background thread: potion addition, player distance increment
    private Timer timer;

    // collection to record places of players, who lose earlier has lower index
    private List<String> plPlaces;

    public State getState() {
        return state;
    }


    /**
     * @@ have to rewrite all comments including this ofc
     * @@ plane maze sizes have to match this' static? sizes(numRows, numCols)
     */
    public GameWorld(GameMaze gm, Configuration config) {
        this(new ArrayList<>(), gm, config, false);
    }


    /**
     * construct game object. It has two states on and off, represented with running
     * variable, iff game is on: players cannot move from old distance on too far new distances, request
     * will be just ignored, player will be on same place; cannot add new players; cannot start game(again).
     * @param players names of players ki bijos
     * @param gm abstract representation of maze, represents some maze and we can check where are and where are not walls
     * @param startGame user tells to start game or not. if true passed game will start at the end of constructor.
     */
    public GameWorld(Collection<String> players, GameMaze gm, Configuration config, boolean startGame) {
        state = State.NEW;
        this.gm = gm;
        readConfig(config);
        nameOnPlayer = new HashMap<>();
        activePlNum = 0;
        potsToAdd = new HashMap<>();
        potsToRemove = new HashMap<>();
        plPlaces = new ArrayList<>();
        players.forEach(p -> addPlayerAtCorner(p));
        for (int i = 0; i < startPotNum; i++) { addPotAtRandom(false); }
        if (startGame) { startGame(); }
    }

    /**
     * read variables from configuration object
     * @param config configuration object to read info from
     */
    private void readConfig(Configuration config) {
        maxPlayers = config.getMaxPlayers();
        startLifeNum = config.getLifeNum();
        plusDist = config.getPlusDist();
        plusDistDelay = config.getPlusDistDelay();
        startPotNum = config.getStartPotNum();
        addPotDelay = config.getAddPotDelay();
        potForKick = config.getPotForKick();
    }

    /* game creation methods, before start */

    /**
     * Tries to add new player with given name at one of corner, on some random part of cell.
     * First tries up-left corner, then tries next clockwise. If player conflicts with
     * one of players(i.e. it is near to one of player then determined distance and those two
     * have different count of potions) or potion(i.e. it overlaps one of potions), tries
     * next corner, if does not finds proper corner does not adds new player. If already
     * added max count of players, or if already have player with that name, false returned.
     * By default: new players active state is true, potion number is zero.
     * @param name name of new player to add
     * @return true iff player added
     */
    @Override
    public synchronized boolean addPlayerAtCorner(String name) {
        if (!canAddPlayer(name)) { return false; }
        if (gm.addPlayerAtCorner(name)){
            addPlayer(new Player(name, startLifeNum, nameOnPlayer.size()));
            return true;
        }
        return false;
    }


    /**
     * Tries to add new player with given name on some random cell in random position
     * in that cell. If player conflicts with one of players(i.e. it is near to one of
     * player then determined distance and those two have different count of potions) or
     * potion(i.e. it overlaps one of potions) tries other cell xor position. If already
     * added max count of players, or if already have player with that name, false returned.
     * By default: new players active state is true, potion number is zero.
     * @param name name of new player to try addition
     * @return true iff player added
     */
    public synchronized boolean addPlayerAtRandom(String name) {
        if (!canAddPlayer(name)) { return false; }
        if (gm.addPlayerAtRandom(name)){
            addPlayer(new Player(name, startLifeNum, nameOnPlayer.size()));
            return true;
        }
        return false;
    }

    /**
     * Tries to add new player with given name on given cell, on some random part of cell,
     * if given cell is out of bounds runtime exception will be thrown. If player conflicts with
     * one of players(i.e. it is near to one of player then determined distance and those two
     * have different count of potions) or potion(i.e. it overlaps one of potions), then it
     * will not be added. If already added max count of players, or if already have player with
     * that name, false returned. By default: new players active state is true, potion number is zero.
     * @param name name of new player to add
     * @param c cell on which to add new player
     * @return true iff player added
     */
    public synchronized boolean addPlayerInCell(String name, Cell c) {
        if (!canAddPlayer(name)) { return false; }
        if (gm.addPlayerInCell(name, c)){
            addPlayer(new Player(name, startLifeNum, nameOnPlayer.size()));
            return true;
        }
        return false;
    }

    private boolean canAddPlayer(String name) {
        return state == State.NEW
                && nameOnPlayer.size() < maxPlayers
                && !nameOnPlayer.keySet().contains(name);
    }

    private void addPlayer(Player p) {
        nameOnPlayer.put(p.getName(), p);
        activePlNum++;
        initUpdateVars(p.getName());
    }

    private void initUpdateVars(String name) {
        potsToAdd.put(name, Collections.synchronizedSet(new HashSet<>()));
        gm.getPotions().forEach(pot -> potsToAdd.forEach((playerName, list) -> list.add(pot)));
        potsToRemove.put(name, Collections.synchronizedList(new ArrayList<>()));
    }

    public synchronized Point2D.Double addPotAtRandom(boolean conflictAllowed) {
        Point2D.Double pot = gm.addPotAtRandom(conflictAllowed);
        if (pot != null) {
            potsToAdd.forEach((name, set) -> set.add(pot));
        }
        return pot;
    }

    public synchronized boolean addPotInCell(Cell c, boolean conflictAllowed) {
        Point2D.Double pot = gm.addPotInCell(c, conflictAllowed);
        if (pot != null) {
            potsToAdd.forEach((name, set) -> set.add(pot));
            return true;
        }
        return  false;
    }

    /* methods after game has been started */

    @Override
    public void startGame() {
        if (state == State.NEW) {
            state = State.RUNNING;
            timer = new Timer();
            incDistPeriodically();
            addPotPeriodically();
        }
    }

    private void incDistPeriodically() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                gm.increaseDist(plusDist);
                playersPlayers();
                gameOnCheck();
                System.out.println("dist increased: " + gm.getDist());
            }
        }, 0, plusDistDelay);
    }

    private void addPotPeriodically() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Point2D.Double pot = addPotAtRandom(true);
                potsToAdd.forEach((name, set) -> set.add(pot));
                playersPot(pot);
                gameOnCheck();
                System.out.println("Potion added");
            }
        }, 0, addPotDelay);
    }

    @Override
    public boolean playerMove(String playerName, double dx, double dy) {
        gm.plusPlayerPos(playerName, dx, dy);
        Point2D.Double pos = gm.getPlPosition(playerName);
        setPlayerCoordinates(playerName, pos.x + dx, pos.y + dy);
        return true;
    }

    @Override
    public synchronized boolean setPlayerCoordinates(String playerName, double x, double y) {
        Player p = nameOnPlayer.get(playerName);
        if (!p.getActive()) { return false; }
        if (state == State.RUNNING && gm.longMove(p.getName(), x, y)) { return false; }
        //if (gm.collideWall(p.getName(), x, y)) { return false; }

        gm.setPlayerPos(playerName, x, y);

        potionsPlayer(p);
        playersPlayer(p);
        gameOnCheck();

        return true;
    }

    private void playersPlayers() {
        Collection<Player> players = nameOnPlayer.values();
        players.forEach(p -> {
            if (p.getActive()) {
                playersPlayer(p);
            }
        });
    }

    /*
     * @@ passed player must be active
     */
    private synchronized void playersPlayer(Player p) {
        Collection<String> collidedPlayers = gm.collidedPlayers(p.getName());
        Iterator<String> colPlsIt = collidedPlayers.iterator();
        while (colPlsIt.hasNext()) {
            String nextName = colPlsIt.next();
            Player colPl = nameOnPlayer.get(nextName);
            if (p.isWinner(colPl)) {
                kickPlayer(p, colPl);
            } else {
                kickPlayer(colPl, p);
                break;
            }
        }
    }

    private void playersPot(Point2D.Double pot) {
        Collection<String> collidedPlayers = gm.collidedPlayers(pot);
        collidedPlayers.forEach(n -> {
            Player p = nameOnPlayer.get(n);
            p.potionPlus();
            gm.removePot(pot);
            potsToRemove.forEach((name, list) -> list.add(pot.hashCode()));
            playersPlayer(p);
        });
    }

    /*
     * @@ must be active player
     */
    private void potionsPlayer(Player player) {
        Collection<Point2D.Double> collidedPots = gm.collidedPotions(player.getName());
        collidedPots.forEach(pot -> {
            player.potionPlus();
            gm.removePot(pot);
            potsToRemove.forEach((name, list) -> list.add(pot.hashCode()));
            playersPlayer(player);
        });
    }

    private void gameOnCheck() {
        if (activePlNum < 2) {
            System.out.println("GameWorld(gameOnCheck): game over");
            finishGame();
        }
    }

    private void kickPlayer(Player kicker, Player toKick) {
        toKick.decreaseLifeNum();
        if (toKick.getLifeNum() == 0) {
            toKick.setActive(false);
            gm.removePlayer(toKick.getName());
            plPlaces.add(toKick.getName());
            activePlNum--;
        } else {
            gm.resetPlace(toKick.getName());
        }
        if (state == State.RUNNING) {
            kicker.setPotNum(kicker.getPotNum() + potForKick);
        }

    }

    @Override
    public int numberOfPlayers() {
        return nameOnPlayer.size();
    }

    @Override
    public Collection<String> getPlayers() {
        return Collections.unmodifiableCollection(nameOnPlayer.keySet());
    }

    /**
     * returns collection in which are kicked players, player which
     * lose earlier has lower index
     *
     * @return collection of kicked players, player which lose earlier has lower index
     */
    @Override
    public List<String> playerPlaces() {
        return Collections.unmodifiableList(plPlaces);
    }

    @Override
    public boolean isFinished() {
        return state == State.FINISHED;
    }

    @Override
    public JsonObject getInit() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonObjectBuilder initJson = factory.createObjectBuilder();

        initJson.add("type", "INIT");

        JsonObjectBuilder plTypesJson = factory.createObjectBuilder();
        Iterator<String> plIt = nameOnPlayer.keySet().iterator();
        while (plIt.hasNext()) {
            String nextName = plIt.next();
            plTypesJson.add(nextName, nameOnPlayer.get(nextName).getType());
        }
        initJson.add("playerTypes", plTypesJson);

        JsonArrayBuilder potsJson = factory.createArrayBuilder();
        Iterator<Point2D.Double> potIt = gm.getPotions().iterator();
        while (potIt.hasNext()) {
            Point2D.Double nextPot = potIt.next();
            JsonObjectBuilder potJson = factory.createObjectBuilder();
            potJson.add("id", nextPot.hashCode())
                    .add("x", nextPot.x)
                    .add("y", nextPot.y);
            potsJson.add(potJson);
        }
        initJson.add("potions", potsJson);

        JsonObjectBuilder mazeJson = gm.toJsonBuilder();
        initJson.add("planeMaze", mazeJson);

        JsonObjectBuilder configJson = gm.configJsonBuilder();
        initJson.add("configuration", configJson);


        return initJson.build();
    }

    @Override
    public synchronized JsonObject getUpdate(String playerName) {

        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonObjectBuilder updateJson = factory.createObjectBuilder();

        updateJson.add("type", "UPDATE");

        updateJson.add("finished", isFinished());

        if (isFinished() == true) {
            JsonObjectBuilder resultsJson = factory.createObjectBuilder();
            for (int i = 0; i < plPlaces.size(); i++) {
                String name = plPlaces.get(i);
                JsonObjectBuilder resultJson = factory.createObjectBuilder();
                resultJson.add("place", i)
                        .add("potNum", nameOnPlayer.get(name).getPotNum());
                resultsJson.add(name, resultJson);
            }
            updateJson.add("results", resultsJson);
        }

        updateJson.add("potNum", nameOnPlayer.get(playerName).getPotNum());

        // create and add json players' array
        JsonArrayBuilder playersJson = factory.createArrayBuilder();
        Iterator<String> plIt = nameOnPlayer.keySet().iterator();
        while (plIt.hasNext()) {
            String nextName = plIt.next();
            JsonObjectBuilder playerJson = nameOnPlayer.get(nextName).toJsonBuilder();
            Point2D.Double pos = gm.getPlPosition(nextName);
            JsonObjectBuilder plPosJson = factory.createObjectBuilder();
            plPosJson.add("x", pos.x).add("y", pos.y);
            playerJson.add("position", plPosJson);
            playersJson.add(playerJson);
        }
        updateJson.add("players", playersJson);

        // create and add json addPots' array
        JsonArrayBuilder addPotsJson = factory.createArrayBuilder();
        potsToAdd.get(playerName).forEach(pot -> {
                    JsonObjectBuilder potJson = factory.createObjectBuilder();
                    potJson.add("id", pot.hashCode())
                    .add("x", pot.x).add("y", pot.y);
                    addPotsJson.add(potJson);
        });
        potsToAdd.get(playerName).clear();
        updateJson.add("addPots", addPotsJson);


        // create and add json removePots' array
        JsonArrayBuilder removePotsJson = factory.createArrayBuilder();
        Iterator<Integer> idIt = potsToRemove.get(playerName).iterator();
        while (idIt.hasNext()) {
            removePotsJson.add(idIt.next());
            idIt.remove();
        }
        updateJson.add("removePots", removePotsJson);


        // add json distance double
        updateJson.add("distance", gm.getDist());

        return updateJson.build();
    }

    public double getMinDist() {
        return gm.getDist();
    }

    @Override
    public void finishGame() {
        if (state == State.RUNNING) {
            timer.cancel();
            state = State.FINISHED;
        }
    }

}





























