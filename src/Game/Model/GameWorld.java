package Game.Model;

import javax.json.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
    private static final String fileName = "ConfigFile.properties";

    // number of corners of game map
    private static final int CORNER_NUM = 4;

    // random instance to generate pseudo random stuff
    private static final Random rand = new Random();

    // enum of states of this class instance
    enum State {NEW, RUNNING, FINISHED}


    // variables to read from configuration object
    private int maxPlayers;
    private int numRows;
    private int numCols;
    private double width;
    private double height;
    private double wallWidth;
    private double pRadius;
    private double maxMove;
    private double startDist;
    private double plusDist;
    private long plusDistDelay;
    private double potRadius;
    private int startPotNum;
    private long addPotDelay;
    private int potForKick;
    private double cellWidth;
    private double cellHeight;

    // state of this class
    private State state;

    // maze object which has raw info about maze for this game world
    private PlaneMaze pm;

    // getting starting value from configuration info for dist
    private double dist;

    // fill depended on info passed in constructor
    private ConcurrentMap<String, Player> nameOnPlayer;

    // coordinates of potions, up-left point of rect surrounding circle
    private List<Point2D.Double> potions;

    // new potions to send by json
    private Map<String, Set<Point2D.Double>> potsToAdd;

    // potions to remove from display, send as json
    private Map<String, List<Integer>> potsToRemove;

    // number of player added at one corners of game world
    private int playersAtCorner;

    // getting value according to number of active players
    private int activePlNum;

    // when game starts, starts two task in background thread: potion addition, player distance increment
    private Timer timer;

    // collection to record places of players, who lose earlier has lower index
    List<String> plPlaces;

    /**
     * constructor for testing purposes only
     */
    public GameWorld(PlaneMaze pm, Configuration config, ConcurrentMap<String, Player> nameOnPlayer, List<Point2D.Double> potions, State state) {
        this(new ArrayList<>(), pm, config, false);
        this.nameOnPlayer = nameOnPlayer;
        this.potions = potions;
        this.state = state;
    }

    public State getState() {
        return state;
    }


    /**
     * @@ have to rewrite all comments including this ofc
     * @@ plane maze sizes have to match this' static sizes(numRows, numCols)
     */
    public GameWorld(PlaneMaze pm, Configuration config) {
        this(new ArrayList<>(), pm, config, false);
    }


    /**
     * construct game object. It has two states on and off, represented with running
     * variable, iff game is on: players cannot move from old distance on too far new distances, request
     * will be just ignored, player will be on same place; cannot add new players; cannot start game(again).
     * @param players names of players ki bijos
     * @param pm abstract representation of maze, represents some maze and we can check where are and where are not walls
     * @param startGame user tells to start game or not. if true passed game will start at the end of constructor.
     */
    public GameWorld(Collection<String> players, PlaneMaze pm, Configuration config, boolean startGame) {
        state = State.NEW;
        this.pm = pm;
        readConfig(config);
        dist = startDist;
        nameOnPlayer = new ConcurrentHashMap<>();
        activePlNum = 0;
        potions = Collections.synchronizedList(new ArrayList<>());
        potsToAdd = new HashMap<>();
        potsToRemove = new HashMap<>();
        plPlaces = Collections.synchronizedList(new ArrayList<>());
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
        numRows = config.getNumRows();
        numCols = config.getNumCols();
        width = config.getWidth();
        height = config.getHeight();
        wallWidth = config.getWallWidth();
        pRadius = config.getPRadius();
        maxMove = config.getMaxMove();
        startDist = config.getStartDist();
        plusDist = config.getPlusDist();
        plusDistDelay = config.getPlusDistDelay();
        potRadius = config.getPotRadius();
        startPotNum = config.getStartPotNum();
        addPotDelay = config.getAddPotDelay();
        potForKick = config.getPotForKick();
        cellWidth = config.getCellWidth();
        cellHeight = config.getCellHeight();
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
        Player p = new Player(name, nameOnPlayer.size());
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
        Player p = new Player(name, nameOnPlayer.size());
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
        Integer[] cells = new Integer[numRows * numCols];
        for (int i = 0; i < numRows * numCols; i++) {
            cells[i] = i;
        }
        shuffleArray(cells);
        boolean added = false;
        for (int i = 0; i < cells.length; i++) {
            Cell nextRandCell = new Cell(cells[i] / numCols, cells[i] % numCols);
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
        boolean added = false;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final Point2D.Double pot = randOvalInCell(c, potRadius);
            if (!potionConflicts(pot)) {
                potions.add(pot);
                potsToAdd.forEach((name, set) -> set.add(pot));
                added = true;
                break;
            }
        }
        return added;
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
                c = new Cell(0, numCols - 1);
                break;
            case 2:
                c = new Cell(numRows - 1, numCols - 1);
                break;
            case 3:
                c = new Cell(numRows - 1, 0);
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
                initUpdateVars(p.getName());
                activePlNum++;
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

    private void initUpdateVars(String name) {
        potsToAdd.put(name, Collections.synchronizedSet(new HashSet<>()));
        potions.forEach(pot -> {
            potsToAdd.forEach((playerName, list) -> list.add(pot));
        });
        potsToRemove.put(name, Collections.synchronizedList(new ArrayList<>()));
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
            timer = new Timer();
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
        potsToAdd.forEach((name, set) -> set.add(pot));
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

        //if (wrongPlace(x, y)) { return false; }

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
                if (Player.getWinner(p, player).equals(p)) {
                    kickPlayer(p, player);
                    playersPlayer(p);
                } else {
                    kickPlayer(player, p);
                    playersPlayer(player);
                }
                return true;
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
                    potsToRemove.forEach((name, list) -> list.add(pot.hashCode()));
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
                    potsToRemove.forEach((name, list) -> list.add(nextPot.hashCode()));
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
            System.out.println("game over");
            finishGame();
        }
    }

    private void kickPlayer(Player kicker, Player toKick) {
        toKick.setActive(false);
        plPlaces.add(toKick.getName());
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

    private boolean wrongPlace(Double x, Double y) {
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

        JsonArrayBuilder plTypesJson = factory.createArrayBuilder();
        Iterator<String> plIt = nameOnPlayer.keySet().iterator();
        while (plIt.hasNext()) {
            String nextName = plIt.next();
            JsonObjectBuilder plTypeJson = factory.createObjectBuilder();
            plTypeJson.add("name", nextName)
                    .add("type", nameOnPlayer.get(nextName).getType());

            plTypesJson.add(plTypeJson);
        }
        initJson.add("playerTypes", plTypesJson);


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

        // create and add json players' array
        JsonArrayBuilder playersJson = factory.createArrayBuilder();
        Iterator<String> plIt = nameOnPlayer.keySet().iterator();
        while (plIt.hasNext()) {
            String nextName = plIt.next();
            JsonObjectBuilder playerJson = nameOnPlayer.get(nextName).toJsonBuilder();
            playersJson.add(playerJson);
        }
        updateJson.add("players", playersJson);

        // create and add json addPots' array
        JsonArrayBuilder addPotsJson = factory.createArrayBuilder();
        synchronized (potsToAdd.get(playerName)) {
            potsToAdd.get(playerName).forEach(pot -> {
                JsonObjectBuilder potJson = factory.createObjectBuilder();
                potJson.add("id", pot.hashCode())
                        .add("x", pot.getX())
                        .add("y", pot.getY());
                addPotsJson.add(potJson);
            });
            potsToAdd.get(playerName).clear();
            updateJson.add("addPots", addPotsJson);
        }


        // create and add json removePots' array
        JsonArrayBuilder removePotsJson = factory.createArrayBuilder();
        synchronized (potsToRemove.get(playerName)) {
            Iterator<Integer> idIt = potsToRemove.get(playerName).iterator();
            while (idIt.hasNext()) {
                removePotsJson.add(idIt.next());
                idIt.remove();
            }
            updateJson.add("removePots", removePotsJson);
        }


        // add json distance double
        updateJson.add("distance", dist);

        return updateJson.build();
    }

    public double getMinDist() {
        return dist;
    }

    @Override
    public void finishGame() {
        if (state == State.RUNNING) {
            timer.cancel();
            state = State.FINISHED;
        }
    }

}





























