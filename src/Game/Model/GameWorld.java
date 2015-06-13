package Game.Model;

import javax.json.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by SHAKO on 30-May-15.
 */
public class GameWorld implements iWorld {

    // file name to read configuration info from
    private static final String fileName = "ConfigFile.txt";

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
            // load a properties file
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

    // random instance to generate pseudo random stuff
    private static final Random rand = new Random();

    /* instance variables */

    // maze object which has raw info about maze for this game world
    private PlaneMaze pm;

    // have to calculate depended on configuration info
    public final double cellWidth;
    public final double cellHeight;

    // getting starting value from configuration info for dist
    private double dist;

    // fill depended on info passed in constructor
    private Map<String, Player> nameOnPlayer;

    // getting value according to number of active players
    private int activePlNum;

    // coordinates of potions, up-left point of rect surrounding circle
    private List<Point2D.Double> potions;

    // if game running or not (for example, if running some things happening periodically)
    private boolean running;

    // when game starts, starts two task in background thread: potion addition, player distance increment
    private Timer timer;


    /**
     * @@ have to rewrite all comments including this ofc
     * @@ plane maze sizes have to match this' static sizes(numRows, numCols)
     * @param pm
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
    public GameWorld(Collection<Player> players, PlaneMaze pm, boolean startGame) {
        running = false; // until world constructor finishes clearly game is not on

        this.pm = pm;

        cellWidth = ((width - (numCols - 1) * wallWidth)) / numCols;
        cellHeight = ((height - (numRows - 1) * wallWidth)) / numRows;

        dist = startDist;

        nameOnPlayer = new HashMap<>();
        potions = Collections.synchronizedList(new ArrayList<>());

        players.forEach(p -> addPlayer(p));

        for (int i = 0; i < startPotNum; i++) {
            addPotAtRand();
        }

        try {
            checkConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (startGame) {
            startGame();
        }
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    @Override
    public boolean addPlayer(String playerName) {
        if (!running && nameOnPlayer.size() < maxPlayers && !nameOnPlayer.containsKey(playerName)) {
            Player p = new Player(playerName);
            return addPlayer(p);
        }
        return false;
    }


    /**
     * @@ must have start pos if start cell type is "given"
     * if player added it has positiion
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    @Override
    public boolean addPlayer(Player player) {
        if (!running && nameOnPlayer.size() < maxPlayers && !nameOnPlayer.containsKey(player.getName())) {
            Player.StartCellT sct = player.getStartCellT();
            switch (sct) {
                case atCorner:
                    player.setPosition(getCornerPos(nameOnPlayer.size()));
                    break;
                case given:
                    Cell startCell = player.getStartCell();
                    if (startCell.row < 0 || startCell.row > numRows - 1 ||
                            startCell.col < 0 || startCell.col > numCols - 1) {
                        return false;
                    }
                    player.setPosition(randOvalInCell(startCell, pRadius));
                    break;
                case random:
                    player.setPosition(randOval(pRadius));
                    break;
                default:
                    break;
            }

            nameOnPlayer.put(player.getName(), player);
            activePlNum++;
            return true;
        }
        return false;
    }

    /**
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private Point2D.Double getCornerPos(int size) {
        Point2D.Double pos;
        switch (size) {
            case 0:
                pos = new Point2D.Double((cellWidth - 2 * pRadius) / 2,
                        (cellHeight - 2 * pRadius) / 2);
                break;
            case 1:
                pos = new Point2D.Double((numCols - 1) * cellWidth + (numCols - 1) * wallWidth + (cellWidth - 2 * pRadius) / 2,
                        (cellHeight - 2 * pRadius) / 2);
                break;
            case 2:
                pos = new Point2D.Double((numCols - 1) * cellWidth + (numCols - 1) * wallWidth + (cellWidth - 2 * pRadius) / 2,
                        (numRows - 1) * cellHeight + (numRows - 1) * wallWidth + (cellHeight - 2 * pRadius) / 2);
                break;
            case 3:
                pos = new Point2D.Double((cellWidth - 2 * pRadius) / 2,
                        (numRows - 1) * cellHeight + (numRows - 1) * wallWidth + (cellHeight - 2 * pRadius) / 2);
                break;
            default:
                return null;
        }
        return pos;
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


    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private void checkConfig() throws Exception {
        if (2 * pRadius > cellWidth || 2 * pRadius > cellHeight) {
            throw new Exception("Player radius is too large for corridor");
        }

        if (2 * potRadius > cellWidth || 2 * potRadius > cellHeight) {
            throw new Exception("Potion radius is too large for corridor");
        }
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    @Override
    public void startGame() {
        if (!running) {
            running = true;
            timer = new Timer(true);
            incDistPeriodically();
            addPotPeriodically();
        }
    }
    // @@game on checkebi
    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private void incDistPeriodically() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                dist += plusDist;
                playersPlayers();
                gameOnCheck();
                System.out.println("dist increased: " + dist);
            }
        }, 0, plusDistDelay);
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private void playersPlayers() {
        Collection<Player> players = nameOnPlayer.values();
        players.forEach(p -> {
            if (p.getActive()) {
                playersPlayer(p);
            }
        });
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private void addPotPeriodically() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Point2D.Double pot = addPotAtRand();
                playersPot(pot);
                gameOnCheck();
                System.out.println("Potion added");
            }
        }, 0, addPotDelay);
    }

    /**
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    public Point2D.Double addPotAtRand() {
        Point2D.Double pot = randOval(potRadius);
        potions.add(pot);
        return pot;
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private Point2D.Double randOval(double radius) {
        int randRow = rand.nextInt(numRows);
        int randCol = rand.nextInt(numCols);
        return randOvalInCell(new Cell(randRow, randCol), radius);
    }

    public Point2D.Double addPotInCell(Cell c) {
        Point2D.Double pot = randOvalInCell(c, potRadius);
        potions.add(pot);
        return pot;
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private Point2D.Double randOvalInCell(Cell c, double radius) {
        double rXInCell = randDouble(0, cellWidth - 2 * radius);
        double rYInCell = randDouble(0, cellHeight - 2 * radius);

        return new Point2D.Double((cellWidth + wallWidth) * c.col + rXInCell,
                (cellHeight + wallWidth) * c.row + rYInCell);
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private void playersPot(Point2D.Double pot) {
        Collection<Player> players = nameOnPlayer.values();
        players.stream().anyMatch(p -> {
            if (p.getActive()) {
                Point2D.Double plPos = p.getPosition();
                if (distance(plPos.x + pRadius, plPos.y + pRadius, pot.x + potRadius, pot.y + potRadius) < pRadius + potRadius) {
                    p.potionPlus();
                    potions.remove(pot);
                    playersPlayer(p);
                    return true;
                }
            }
            return false;
        });
    }



    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    @Override
    public boolean playerMove(String playerName, double dx, double dy) {
        Point2D.Double pos = nameOnPlayer.get(playerName).getPosition();
        setPlayerCoordinates(playerName, pos.getX() + dx, pos.getY() + dy);
        return true;
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    @Override
    public boolean setPlayerCoordinates(String playerName, double x, double y) {
        Player p = nameOnPlayer.get(playerName);
        Point2D.Double pos = p.getPosition();
       /* if (running) {
            if (distance(x, y, pos.getX(), pos.getY()) > maxMove) {
                return false;
            }
        }*/
//        if (wrongPlace(x, y)) {
//            return false;
//        }

        p.setPosition(x, y);

        if (p.getActive()) {
            potionsPlayer(p);
            playersPlayer(p);
            gameOnCheck();
        }
        return true;
    }

    /**
     * @@ must be active player
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
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
                    if (!somePotTaken) {
                        somePotTaken = true;
                    }
                    playersPlayer(player);
                }
            }
        }
        return somePotTaken;
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
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

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
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

    /**
     * @@ have to rewrite all comments including this ofc
     * @@ have to be adjacent
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
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

    /**
     * @@ must not be out of bounds non of them
     * @@ have to be diagonally adjacent
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
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

    /**
     * @@ have to rewrite all comments including this ofc
     * @@ have to be diagonally adjacent
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
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

    /**
     * @@ passed player must be active
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
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

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private void kickPlayer(Player kicker, Player toKick) {
        toKick.setActive(false);
        if (running) {
            kicker.setPotNum(kicker.getPotNum() + potForKick);
        }
        activePlNum--;
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private void gameOnCheck() {
        if (activePlNum < 2) {
            running = false;
            finishGame();
        }
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    @Override
    public int numberOfPlayers() {
        return nameOnPlayer.size();
    }

    /**
     * @@ if no player with this name null will be returned bitch!
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    public Player getPlayer(String playerName) {
        return nameOnPlayer.get(playerName);
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    @Override
    public Collection<String> getPlayerNames() {
        return Collections.unmodifiableCollection(nameOnPlayer.keySet());
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    @Override
    public Collection<Player> getPlayers() {
        return Collections.unmodifiableCollection(nameOnPlayer.values());
    }


    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    @Override
    public boolean gameOn() {
        return true;
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
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

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    @Override
    public JsonObject getUpdate(String playerName) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonObjectBuilder updateJson = factory.createObjectBuilder();

        updateJson.add("type", "UPDATE");

        updateJson.add("gameOn", gameOn());

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

        // create/add json potions' array
        JsonArrayBuilder potsJson = factory.createArrayBuilder();
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
        updateJson.add("potions", potsJson);

        // add json distance double
        updateJson.add("distance", dist);

        return updateJson.build();
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    @Override
    public void finishGame() {
        if (running) {
            timer.cancel();
            running = false;
        }
    }

}































