package Game.Model;

import javax.json.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by SHAKO on 30-May-15.
 * GameWorld class, for creating and interacting with game,
 * changing and getting state of it.
 */
public class GameWorld implements iWorld {

    // file name to read configuration info from
    private static final String fileName = "ConfigFile.txt";

    // number of corners of game map
    private static final int CORNER_NUM = 4;

    // static final variables whose values read from configuration file
    public static final int maxPlayers;
    public static final int numRows;
    public static final int numCols;
    public static final double width;
    public static final double height;
    public static final double wallWidth;
    public static final double pRadius;
    public static final double maxMove;
    public static final double startDist;
    public static final double plusDist;
    public static final long plusDistDelay;
    public static final double potRadius;
    public static final int startPotNum;
    public static final long addPotDelay;
    public static final int potForKick;

    // have to calculate depended on configuration info
    public static final double cellWidth;
    public static final double cellHeight;

    // static block
    static {
        // object to write info from file into, and read from it then
        Properties prop = new Properties();
        loadFromFile(prop, fileName);

        // reading info into public static final variables from properties object
        maxPlayers = Integer.parseInt(prop.getProperty("maxPlayers"));
        numRows = Integer.parseInt(prop.getProperty("numRows"));
        numCols = Integer.parseInt(prop.getProperty("numCols"));
        width = Double.parseDouble(prop.getProperty("width"));
        height = Double.parseDouble(prop.getProperty("height"));
        wallWidth = Double.parseDouble(prop.getProperty("wallWidth"));
        pRadius = Double.parseDouble(prop.getProperty("pRadius"));
        maxMove = Double.parseDouble(prop.getProperty("maxMove"));
        startDist = Double.parseDouble(prop.getProperty("startDist"));
        plusDist = Double.parseDouble(prop.getProperty("plusDist"));
        plusDistDelay = Long.parseLong(prop.getProperty("plusDistDelay"));
        potRadius = Double.parseDouble(prop.getProperty("potRadius"));
        startPotNum = Integer.parseInt(prop.getProperty("startPotNum"));
        addPotDelay = Long.parseLong(prop.getProperty("addPotDelay"));
        potForKick = Integer.parseInt(prop.getProperty("potForKick"));

        // calculate some values depended on value read from file
        cellWidth = ((width - (numCols - 1) * wallWidth)) / numCols;
        cellHeight = ((height - (numRows - 1) * wallWidth)) / numRows;
        try {
            checkConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * writes info from file, with given name, into given properties object
     * @param prop write info from specified file into this
     * @param fileName searches file with this name to load info from into given properties object
     */
    private static void loadFromFile(Properties prop, String fileName) {
        InputStream input = null;
        try {
            input = GameWorld.class.getClassLoader().getResourceAsStream(fileName);
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * checks if configuration file is well write, for example
     * that double player darius is not more than corridor width
     * or height. If finds some mistate throws proper runtime exception.
     */
    private static void checkConfig() {
        if (2 * pRadius > cellWidth || 2 * pRadius > cellHeight) {
            throw new RuntimeException("Player radius is too large for corridor");
        }

        if (2 * potRadius > cellWidth || 2 * potRadius > cellHeight) {
            throw new RuntimeException("Potion radius is too large for corridor");
        }
    }

    // random instance to generate pseudo random stuff
    private static final Random rand = new Random();

    // enum of states of this class instance
    enum State {NEW, RUNNING, FINISHED}

    /* instance variables */

    // state of this class
    private State state;

    // maze object which has raw info about maze for this game world
    private PlaneMaze pm;

    // getting starting value from configuration info for dist
    private double dist;

    // fill depended on info passed in constructor
    private ConcurrentMap<String, Player> nameOnPlayer;

    // coordinates of potions, up-left point of rect surrounding circle
    private final List<Point2D.Double> potions;

    // number of player added at one corners of game world
    private int playersAtCorner;

    // getting value according to number of active players
    private int activePlNum;

    // when game starts, starts two task in background thread: potion addition, player distance increment
    private Timer timer;

    // iff potions added or removed after last getUpdate method call this is true
    private boolean potsUpdated;

    // lastly generated potions json, if update did not happen will not generate again
    JsonArrayBuilder potsJson;

    /**
     * @@ have to rewrite all comments including this ofc
     * @@ plane maze sizes have to match this' static sizes(numRows, numCols)
     */
    public GameWorld(PlaneMaze pm) {
        this(new ArrayList<>(), pm, false);
    }


    /**
     * construct game object. It has two states on and off, represented with running
     * variable, iff game is on: players cannot move from old distance on too far new distances, request
     * will be just ignored, player will be on same place; cannot add new players; cannot start game(again).
     * @param players names of players ki bijos
     * @param pm abstract representation of maze, represents some maze and we can check where are and where are not walls
     * @param startGame user tells to start game or not. if true passed game will start at the end of constructor.
     */
    public GameWorld(Collection<String> players, PlaneMaze pm, boolean startGame) {
        state = State.NEW;
        this.pm = pm;
        dist = startDist;
        nameOnPlayer = new ConcurrentHashMap<>();
        potions = Collections.synchronizedList(new ArrayList<>());
        potsUpdated = false;
        players.forEach(p -> addPlayerAtCorner(p));
        for (int i = 0; i < startPotNum; i++) { addPotAtRandom(false); }
        if (startPotNum != 0) { potsUpdated = true; }
        potsJson = Json.createArrayBuilder();
        if (startGame) { startGame(); }
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
        if (nameOnPlayer.size() >= maxPlayers ||
                nameOnPlayer.keySet().contains(name)) {
            return false;
        }
        Player p = new Player(name);
        boolean added = false;
        for (int i = playersAtCorner; i < CORNER_NUM; i++) {
            if (tryPlayerInCell(p, getCornerCell(i))) {
                playersAtCorner++;
                added = true;
                break;
            }
        }
        return added;
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
        if (nameOnPlayer.size() >= maxPlayers ||
                nameOnPlayer.keySet().contains(name)) {
            return false;
        }
        // TODO method code bitch !
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
        Player p = new Player(name);
        boolean placeFound = false;
        if (tryPlayerInCell(p, c)) {
            placeFound = true;
            if (isCorner(c)) {
                playersAtCorner++;
            }
        }
        return placeFound;
    }

    /**
     * removes player from game, if game does not contain player with given name
     * null will be returned.
     * @param name name of player to remove from game if is in game
     * @return player which has given name, or null if there is no player with given name
     */
    public synchronized Player removePlayer(String name) {
        return nameOnPlayer.remove(name);
    }


    /**
     * Tries to add potion in random cell, on random position. If potion
     * with some position conflicts with one of players(i.e. potion intersects with
     * one of players) tries another cell xor position.
     * @return true iff potion added
     */
    public synchronized boolean addPotAtRandom(boolean cornerAllowed) {
        Integer[] cells = new Integer[numRows * numCols ];
        for (int i = 0; i < numRows * numCols; i++) {
            cells[i] = i;
        }
        shuffleArray(cells);
        boolean added = false;
        for (int i = 0; i < cells.length; i++) {
            Cell nextRandCell = new Cell(i / numCols, i % numCols);
            if ((cornerAllowed || !isCorner(nextRandCell)) && addPotInCell(nextRandCell)) {
                added = true;
                break;
            }
        }
        return added;
    }

    /**
     * Tries to add potion in given cell, on random position. If potion
     * with some position conflicts with one of players(i.e. potion intersects with
     * one of players) tries another position. If cell is out of bounds of game map
     * proper runtime exception will be thrown.
     * @param c cell to add new potion in
     * @return true iff potion added
     */
    public synchronized boolean addPotInCell(Cell c) {
        if (c.row < 0 || c.row > numRows - 1 ||
                c.col < 0 || c.col > numCols - 1) {
            throw new RuntimeException("Cell is out of bounds!");
        }
        Point2D.Double pot = null;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            pot = randOvalInCell(c, potRadius);
            if (!potionConflicts(pot)) {
                potions.add(pot);
                break;
            }
        }
        return pot != null;
    }

    /**
     * removes last added potion, or null if there are no potions
     * @return last added potion or null if there are no potions
     */
    public synchronized Point2D.Double removeLastPot() {
        if (potions.size() <= 0) {
            return null;
        }
        return potions.remove(potions.size() - 1);
    }

    /* helper methods for game creation methods */

    /**
     * Gets cell for index-th corner of maze, 0th is up-left,
     * indexing is clockwise, index must be in [0, 3] interval,
     * if not runtime exeption will be thrown.
     * @param index index of corner cell to be returned
     * @return cell for index-th corner of maze
     */
    private Cell getCornerCell(int index) {
        Cell c;
        switch (index) {
            case 0:
                c = new Cell(0, 0);
                break;
            case 1:
                c = new Cell(0, GameWorld.numCols - 1);
                break;
            case 2:
                c = new Cell(GameWorld.numRows - 1, GameWorld.numCols - 1);
                break;
            case 3:
                c = new Cell(GameWorld.numRows - 1, 0);
                break;
            default:
                throw new RuntimeException("Index must be in [0, 3] interval!");
        }
        return c;
    }

    /**
     * Tries to add player in given cell on some random location, if player conflicts with
     * one of players(i.e. it is near to one of player then determined distance and those two
     * have different count of potions) or potion(i.e. it overlaps one of potions) does not adds.
     * If cell is out of game map bounds runtime exception thrown. Does not adds if already added
     * max count of players, or already added player with that name.
     * @param p player to try addition in given cell
     * @param c cell in which to try addition of player
     * @return try iff player added
     */
    private boolean tryPlayerInCell(Player p, Cell c) {
        if (c.row < 0 || c.row > numRows - 1 ||
                c.col < 0 || c.col > numCols - 1) {
            throw new RuntimeException("Cell is out of bounds!");
        }
        if (nameOnPlayer.size() >= maxPlayers ||
                nameOnPlayer.keySet().contains(p.getName())) {
            return false;
        }
        boolean placeFound = false;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            p.setPosition(randOvalInCell(c, pRadius));
            if (!playerConflicts(p)) {
                placeFound = true;
                nameOnPlayer.put(p.getName(), p);
                break;
            }
        }
        return placeFound;
    }

    /**
     * Checks if given player conflicts with one of players(i.e.
     * it is near one of active player than given distance and they
     * have different count of potions) or with one of potions(i.e. it
     * intersects one of potion). Given player have to be active, otherwise
     * this method is redundant(false returned immediately if not active)
     * @param player player to check conflict for
     * @return true iff player is active and conflicts with players or potions
     */
    private boolean playerConflicts(Player player) {
        return plConflictPl(player) || plConflictPot(player);
    }

    /**
     * Checks if given player conflicts with one of players(i.e.
     * it is near one of active player than given distance and they have
     * different count of potions). Given player have to be active, otherwise
     * this method is redundant(false returned immediately if not active)
     * @param player player to check conflict for
     * @return true iff player is active and conflicts with players or potions
     */
    private boolean plConflictPl(Player player) {
        if (!player.getActive()) {
            return false;
        }
        Point2D.Double pos = player.getPosition();
        return nameOnPlayer.values().stream().anyMatch(p -> {
            Point2D.Double otherPlPos = p.getPosition();
            return p.getActive() &&
                    distance(otherPlPos.x + pRadius, otherPlPos.y + pRadius, pos.x + pRadius, pos.y + pRadius) < dist &&
                    p.getPotNum() != player.getPotNum();
        });
    }

    /**
     * Checks if given player conflicts with one of potions(i.e. it
     * intersects one of potion). Given player have to be active, otherwise
     * this method is redundant(false returned immediately if not active)
     * @param player player to check conflict for
     * @return true iff player is active and conflicts with players or potions
     */
    private boolean plConflictPot(Player player) {
        if (!player.getActive()) {
            return false;
        }
        Point2D.Double pos = player.getPosition();
        return potions.stream().anyMatch(pot -> {
            if (distance(pot.x + potRadius, pot.y + potRadius, pos.x + pRadius, pos.y + pRadius) < potRadius + pRadius) {
                return true;
            }
            return false;
        });
    }

    /**
     * checks if given cell is one of corners of game map
     * @param c cell to check if it is corner cell
     * @return true iff given cell is one of corner cells of game map
     */
    private boolean isCorner(Cell c) {
        return (c.row == 0 || c.row == numRows - 1) && (c.col == 0 || c.col == numCols - 1);
    }

    /**
     * Checks If potion with some position conflicts with one of players(i.e.
     * potion intersects with one of players).
     * @param pot potion to check conflict
     * @return true iff given player conflicts with one of players
     */
    private boolean potionConflicts(Point2D.Double pot) {
        return nameOnPlayer.values().stream().anyMatch(p -> {
            Point2D.Double plPos = p.getPosition();
            if (p.getActive() &&
                    distance(plPos.x + pRadius, plPos.y + pRadius, pot.x + potRadius, pot.y + potRadius) < potRadius + pRadius) {
                return true;
            }
            return false;
        });
    }

    /**
     * Generates random circle position in given cell, with given radius, so that it does not
     * leave cell bounds. Position is up-left point of square, with perpendicular edges
     * to Ox Oy, surrounding circle.
     * @param c cell in which to generate circle position
     * @param radius radius of circle to generate position of
     * @return position of circle in given cell with given carius
     */
    private Point2D.Double randOvalInCell(Cell c, double radius) {
        double rXInCell = randDouble(0, cellWidth - 2 * radius);
        double rYInCell = randDouble(0, cellHeight - 2 * radius);

        return new Point2D.Double((cellWidth + wallWidth) * c.col + rXInCell,
                (cellHeight + wallWidth) * c.row + rYInCell);
    }

    /**
     * Shuffle given generic type array uniformly
     * @param arr array to shuffle
     * @param <T> type of array(elements in it) to shuffle
     */
    private static <T> void shuffleArray(T[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            // Simple swap
            T temp = arr[index];
            arr[index] = arr[i];
            arr[i] = temp;
        }
    }

    /* methods after game has been started */

    @Override
    public void startGame() {
        if (state == State.NEW) {
            state = State.RUNNING;
            timer = new Timer(true);
            incDistPeriodically();
            addPotPeriodically();
        }
    }

    private void incDistPeriodically() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                incDist();
                System.out.println("dist increased: " + dist);
            }
        }, 0, plusDistDelay);
    }

    private void addPotPeriodically() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                addPotAtRand();
                System.out.println("Potion added");
            }
        }, 0, addPotDelay);
    }

    private void incDist() {
        dist += plusDist;
        playersPlayers();
        gameOnCheck();
    }

    /*
     * @@ if game in in NEW state tries until adds player at random so that it does not intersects with any player
     */
    private synchronized boolean addPotAtRand() {
        Point2D.Double pot = randOval(potRadius);
        potions.add(pot);
        potsUpdated = true;
        playersPot(pot);
        gameOnCheck();
        return true;
    }

    @Override
    public boolean playerMove(String playerName, double dx, double dy) {
        Point2D.Double pos = nameOnPlayer.get(playerName).getPosition();
        setPlayerCoordinates(playerName, pos.getX() + dx, pos.getY() + dy);
        return true;
    }

    @Override
    public boolean setPlayerCoordinates(String playerName, double x, double y) {
        Player p = nameOnPlayer.get(playerName);
        if (!p.getActive()) { return false; }

        Point2D.Double pos = p.getPosition();

        if (state == State.RUNNING &&
                distance(x, y, pos.x, pos.y) > maxMove) { return false; }

        if (wrongPlace(x, y)) { return false; }

        p.setPosition(x, y);

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
    private boolean playersPlayer(Player p) {
        Point2D.Double plPos = p.getPosition();

        Collection<Player> players = nameOnPlayer.values();

        for (Player player : players) {
            Point2D.Double otherPlPos = player.getPosition();
            if (!p.equals(player) &&
                    player.getActive() &&
                    distance(plPos.x + pRadius, plPos.y + pRadius, otherPlPos.x + pRadius, otherPlPos.y + pRadius) < dist) {
                if (p.getPotNum() > player.getPotNum()) {
                    kickPlayer(p, player);
                    playersPlayer(p);
                    return true;
                } else if (p.getPotNum() < player.getPotNum()) {
                    kickPlayer(player, p);
                    playersPlayer(player);
                    return true;
                }
            }
        }
        return false;
    }

    private void playersPot(Point2D.Double pot) {
        Collection<Player> players = nameOnPlayer.values();
        players.stream().anyMatch(p -> {
            if (p.getActive()) {
                Point2D.Double plPos = p.getPosition();
                if (distance(plPos.x + pRadius, plPos.y + pRadius, pot.x + potRadius, pot.y + potRadius) < pRadius + potRadius) {
                    p.potionPlus();
                    potions.remove(pot);
                    potsUpdated = true;
                    playersPlayer(p);
                    return true;
                }
            }
            return false;
        });
    }

    /*
     * @@ must be active player
     */
    private boolean potionsPlayer(Player player) {
        boolean somePotTaken = false;
        Point2D.Double pos = player.getPosition();
        synchronized (potions) {
            Iterator<Point2D.Double> potIt = potions.iterator();
            while (potIt.hasNext()) {
                Point2D.Double nextPot = potIt.next();
                if (distance(pos.x + pRadius, pos.y + pRadius, nextPot.x + potRadius, nextPot.y + potRadius) < pRadius + potRadius) {
                    player.potionPlus();
                    potIt.remove();
                    potsUpdated = true;
                    if (!somePotTaken) {
                        somePotTaken = true;
                    }
                    playersPlayer(player);
                }
            }
        }
        return somePotTaken;
    }

    private void gameOnCheck() {
        if (activePlNum < 2) {
            finishGame();
        }
    }

    private void kickPlayer(Player kicker, Player toKick) {
        toKick.setActive(false);
        if (state == State.RUNNING) {
            kicker.setPotNum(kicker.getPotNum() + potForKick);
        }
        activePlNum--;
    }


    private Point2D.Double randOval(double radius) {
        int randRow = rand.nextInt(numRows);
        int randCol = rand.nextInt(numCols);
        return randOvalInCell(new Cell(randRow, randCol), radius);
    }

    /**
     * generates random value between two values [min, max), uniformly distributed.
     * @param min generated value will not be lower
     * @param max  generated value will not be higher
     * @return return double uniformly distributed in interval: [min, max)
     */
    private double randDouble(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /* collision methods */

    private boolean wrongPlace(double x, double y) {
        int rowIndx = (int)((y + pRadius) / (cellHeight + wallWidth));
        if (rowIndx < 0 || y + pRadius > rowIndx * (cellHeight + wallWidth) + cellHeight) {
            return true;
        }

        int colIndx = (int)((x + pRadius) / (cellWidth + wallWidth));
        if (colIndx < 0 || x + pRadius > colIndx * (cellWidth + wallWidth) + cellWidth) {
            return true;
        }

        if (collideAround(x, y, new Cell(rowIndx, colIndx))) {
            return true;
        }

        return false;
    }

    private boolean collideAround(double x, double y, Cell c) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Cell neighbour = new Cell(c.row + i, c.col + j);
                if (c.row == neighbour.row ^ c.col == neighbour.col) {
                    if (!pm.cellInBounds(neighbour) || pm.isWall(c, neighbour)) {
                        if (intersect(x + pRadius, y + pRadius, pRadius, getLongRect(c, neighbour))) {
                            return true;
                        }
                    }
                } else {
                    if (c.row != neighbour.row || c.col != neighbour.col) {
                        if (!pm.cellInBounds(neighbour) || isSmallRect(c, neighbour)) {
                            if (intersect(x + pRadius, y + pRadius, pRadius, getSmallRect(c, neighbour))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /*
     * @@ have to be adjacent
     */
    private Rectangle2D.Double getLongRect(Cell c1, Cell c2) {
        Rectangle2D.Double r;
        if (c1.row == c2.row) {
            int maxRow = Math.max(c1.row, c2.row);
            r = new Rectangle2D.Double(cellWidth * maxRow + wallWidth * (maxRow - 1),
                    (cellHeight + wallWidth) * c1.row,
                    wallWidth,
                    cellHeight);

        } else {
            int maxCol = Math.max(c1.col, c2.col);
            r = new Rectangle2D.Double((cellWidth + wallWidth) * c1.col,
                    cellHeight * maxCol + wallWidth * (maxCol - 1),
                    cellWidth,
                    wallWidth);

        }
        return r;
    }

    /**
     * checks if given circumference and rectangle intersect each other
     * @param cX centre x coordinate of  circumference
     * @param cY centre y coordinate of  circumference
     * @param rad radius of circumference
     * @param rect rectangle to check collision with
     * @return true - iff given circumference and rectangle intersects each other, false otherwise
     */
    private boolean intersect(double cX, double cY, double rad, Rectangle2D.Double rect) {
        double dx = Math.abs(cX - rect.x - rect.width / 2);
        double xDist = rect.width / 2 + rad;
        if (dx > xDist)
            return false;
        double dy = Math.abs(cY - rect.y - rect.height / 2);
        double yDist = rect.height / 2 + rad;
        if (dy > yDist)
            return false;
        if (dx <= rect.width / 2 || dy <= rect.height / 2)
            return true;
        double xCornerDist = dx - rect.width / 2;
        double yCornerDist = dy - rect.height / 2;
        double xCornerDistSq = xCornerDist * xCornerDist;
        double yCornerDistSq = yCornerDist * yCornerDist;
        double maxCornerDistSq = rad * rad;
        return xCornerDistSq + yCornerDistSq <= maxCornerDistSq;
    }

    /*
     * @@ must not be out of bounds non of them
     * @@ have to be diagonally adjacent
     */
    private boolean isSmallRect(Cell c1, Cell c2) {
        int minRow = Math.min(c1.row, c2.row);
        int minCol = Math.min(c1.col, c2.col);
        int maxRow = Math.max(c1.row, c2.row);
        int maxCol = Math.max(c1.col, c2.col);

        Cell upLeft = new Cell(minRow, minCol);
        Cell upRight = new Cell(minRow, minCol + 1);
        Cell downRight = new Cell(maxRow, maxCol);
        Cell downLeft = new Cell(maxRow, maxCol - 1);


        if (pm.isWall(upLeft, upRight)) {
            return true;
        } else if (pm.isWall(upRight, downRight)) {
            return true;
        } else if (pm.isWall(downRight, downLeft)) {
            return true;
        } else if (pm.isWall(downLeft, upLeft)) {
            return true;
        }
        return false;
    }

    /*
     * @@ have to be diagonally adjacent
     */
    private Rectangle2D.Double getSmallRect(Cell c1, Cell c2) {
        Rectangle2D.Double r = new Rectangle2D.Double();
        r.width = wallWidth;
        r.height = wallWidth;

        int minRow = Math.min(c1.row, c2.row);
        int minCol = Math.min(c1.col, c2.col);

        r.x = cellWidth * (minCol + 1) + wallWidth * minCol;
        r.y = cellHeight * (minRow + 1) + wallWidth * minRow;
        return r;
    }

    @Override
    public int numberOfPlayers() {
        return nameOnPlayer.size();
    }

    @Override
    public Collection<String> getPlayers() {
        return Collections.unmodifiableCollection(nameOnPlayer.keySet());
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

        JsonObjectBuilder mazeJson = pm.toJsonBuilder();
        initJson.add("planeMaze", mazeJson);

        JsonObjectBuilder configJson = factory.createObjectBuilder();
        configJson.add("width", width)
                .add("height", height)
                .add("wallWidth", wallWidth)
                .add("pRadius", pRadius)
                .add("potRadius", potRadius);
        initJson.add("configuration", configJson);


        return initJson.build();
    }

    @Override
    public JsonObject getUpdate(String playerName) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonObjectBuilder updateJson = factory.createObjectBuilder();

        updateJson.add("type", "UPDATE");

        updateJson.add("finished", isFinished());

        updateJson.add("potNum", nameOnPlayer.get(playerName).getPotNum());

        // create/add json players' array
        JsonArrayBuilder playersJson = factory.createArrayBuilder();
        Iterator<String> plIt = nameOnPlayer.keySet().iterator();
        while (plIt.hasNext()) {
            String nextName = plIt.next();
            JsonObjectBuilder playerJson = nameOnPlayer.get(nextName).toJsonBuilder();
            playersJson.add(playerJson);
        }
        updateJson.add("players", playersJson);

        updateJson.add("potsUpdated", potsUpdated);

        if (potsUpdated) {
            // create/add json potions' array
            synchronized (potions) {
                Iterator<Point2D.Double> potIt = potions.iterator();
                while (potIt.hasNext()) {
                    Point2D.Double nextPot = potIt.next();
                    JsonObjectBuilder potJson = factory.createObjectBuilder();
                    potJson.add("x", nextPot.getX())
                            .add("y", nextPot.getY());
                    potsJson.add(potJson);
                }
            }
            potsUpdated = false;
        }
        updateJson.add("potions", potsJson);

        // add json distance double
        updateJson.add("distance", dist);

        return updateJson.build();
    }

    @Override
    public void finishGame() {
        if (state == State.RUNNING) {
            timer.cancel();
            state = State.FINISHED;
        }
    }

    /* methods for testing */

    public Player getPlayer(String name) {
        return nameOnPlayer.get(name);
    }

    public List<Point2D.Double> getPotions() {
        return potions;
    }

}





























