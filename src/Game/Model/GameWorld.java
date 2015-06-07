package Game.Model;

import javax.json.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

//@@ rezos vutxra rom worldis(mapis) 0, 0 aris charchos gareshe nawilis zeda-arcxena kutxeshi da ara charchoiana


/**
 * Created by SHAKO on 30-May-15.
 */
public class GameWorld implements iWorld {

    private static final Random rand = new Random();

    private static Properties prop = new Properties();

    public static final int maxPlayers;
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


    static {
        ConfigFile.loadFromFile(prop, ConfigFile.fileName);

        // get the property value and initialize public static final variables of this class
        maxPlayers = Integer.parseInt(prop.getProperty("maxPlayers"));
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

    private PlaneMaze pm;
    private boolean startGame;

    private double cellWidth;
    private double cellHeight;
    private int numRows;
    private int numCols;
    private double dist;
    private int activePlNum;
    private boolean running;

    private Map<String, Player> nameOnPlayer;

    // coordinates of potions
    private ArrayList<Point2D.Double> potions;

    // when game starts starts two task in background thread: potion addition, player distance increment
    private Timer timer;


    public GameWorld(PlaneMaze pm) {
        this(new ArrayList<String>(), pm, false);
    }


    /**
     * construct game object. It has two states on and off, represented with running
     * variable, iff game is on: players cannot move from old distance on too far new distances, request
     * will be just ignored, player will be on same place; cannot add new players; cannot start game(again).
     *
     * @param players   names of players
     * @param pm        abstract representation of maze, represents some maze and we can check where are and where are not walls
     * @param startGame user tells to start game or not. if true passed game will start at the end of constructor.
     */
    public GameWorld(Collection<String> players, PlaneMaze pm, boolean startGame) {
        running = false; // until world constructor finishes clearly game is not on

        this.pm = pm;

        numRows = pm.numRows();
        numCols = pm.numCols();
        cellWidth = (width - (numCols - 1) * wallWidth) / numCols;
        cellHeight = (height - (numRows - 1) * wallWidth) / numRows;
        dist = startDist;
        activePlNum = players.size();

        nameOnPlayer = new HashMap<>();
        players.forEach(p -> addPlayer(p));

        potions = new ArrayList<>();
        for (int i = 0; i < startPotNum; i++) {
            addPotion();
        }

        if (startGame) {
            startGame();
        }
    }

    @Override
    public boolean addPlayer(String playerName) {
        if (!running && nameOnPlayer.size() < maxPlayers && !nameOnPlayer.containsKey(playerName)) {
            Player p = null;
            switch (nameOnPlayer.size()) {
                case 0:
                    p = new Player(playerName,
                            (cellWidth - 2 * pRadius) / 2,
                            (cellHeight - 2 * pRadius) / 2,
                            pRadius,
                            true);
                    break;
                case 1:
                    p = new Player(playerName,
                            (numCols - 1) * cellWidth + (numCols - 1) * wallWidth + (cellWidth - 2 * pRadius) / 2,
                            (cellHeight - 2 * pRadius) / 2,
                            pRadius,
                            true);
                    break;
                case 2:
                    p = new Player(playerName,
                            (numCols - 1) * cellWidth + (numCols - 1) * wallWidth + (cellWidth - 2 * pRadius) / 2,
                            (numRows - 1) * cellHeight + (numRows - 1) * wallWidth + (cellHeight - 2 * pRadius) / 2,
                            pRadius,
                            true);
                    break;
                case 3:
                    p = new Player(playerName,
                            (cellWidth - 2 * pRadius) / 2,
                            (numRows - 1) * cellHeight + (numRows - 1) * wallWidth + (cellHeight - 2 * pRadius) / 2,
                            pRadius,
                            true);
                    break;
                default:
                    return false;
            }
            nameOnPlayer.put(playerName, p);
            return true;
        }
        return false;
    }

    //@@if maxPlayer is more than 4  and adding 5th or higher players put randomly
    @Override
    public JsonObject getInit() { //@@ unda shevamciro positionis double mdzimis shemdeg 2 an 3 cifri mara mainc 3 iyos, damrgvalebit da ara chamojra prosta
        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonObjectBuilder initJson = factory.createObjectBuilder();

        initJson.add("type", "INIT");

        JsonObjectBuilder mazeJson = factory.createObjectBuilder();
        mazeJson.add("numRows", numRows)
                .add("numCols", numCols);
        JsonArrayBuilder wallsJson = factory.createArrayBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (i < numRows - 1) {
                    if (pm.isWall(i, j, i + 1, j)) {
                        JsonObjectBuilder wallJson = factory.createObjectBuilder()
                                .add("cell1", factory.createObjectBuilder()
                                        .add("row", i)
                                        .add("col", j))
                                .add("cell2", factory.createObjectBuilder()
                                        .add("row", i + 1)
                                        .add("col", j));
                        wallsJson.add(wallJson);
                    }
                }
                if (j < numCols - 1) {
                    Point right = new Point(i, j + 1);
                    if (pm.isWall(i, j, i, j + 1)) {
                        JsonObjectBuilder wallJson = factory.createObjectBuilder()
                                .add("cell1", factory.createObjectBuilder()
                                        .add("row", i)
                                        .add("col", j))
                                .add("cell2", factory.createObjectBuilder()
                                        .add("row", i)
                                        .add("col", j + 1));
                        wallsJson.add(wallJson);
                    }
                }
            }
        }
        mazeJson.add("walls", wallsJson);
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

        updateJson.add("gameOn", gameOn());

        updateJson.add("potNum", nameOnPlayer.get(playerName).getPotNum());

        // create/add json players' array
        JsonArrayBuilder playersJson = factory.createArrayBuilder();
        Set<String> players = nameOnPlayer.keySet();
        for (String name : players) {
            Player p = nameOnPlayer.get(name);

            JsonObjectBuilder playerJson = factory.createObjectBuilder();

            playerJson.add("active", p.getActive());

            playerJson.add("name", p.getName());

            Point2D.Double plPos = p.getPosition();
            JsonObjectBuilder plPosJson = factory.createObjectBuilder();
            plPosJson.add("x", plPos.getX()).add("y", plPos.getY());
            playerJson.add("position", plPosJson);

            playersJson.add(playerJson);
        }

        updateJson.add("players", playersJson);

        // create/add json potions' array
        JsonArrayBuilder potsJson = factory.createArrayBuilder();

        for (int i = 0; i < potions.size(); i++) {
            Point2D.Double pot = potions.get(i);
            JsonObjectBuilder potJson = factory.createObjectBuilder();

            potJson.add("x", pot.getX())
                    .add("y", pot.getY());

            potsJson.add(potJson);
        }

        updateJson.add("potions", potsJson);

        // add json distance double
        updateJson.add("distance", dist);

        return updateJson.build();
    }


    @Override
    public int numberOfPlayers() {
        return nameOnPlayer.size();
    }

    @Override //@@ codis gameoreba mivxedo setPlayerLocation-tan aris
    public boolean playerMove(String playerName, double dx, double dy) {
        Point2D.Double pos = nameOnPlayer.get(playerName).getPosition();
        setPlayerCoordinates(playerName, pos.getX() + dx, pos.getY() + dy);
        return true;
    }

    @Override
    public boolean setPlayerCoordinates(String playerName, double x, double y) {
        Player p = nameOnPlayer.get(playerName);
        Point2D.Double pos = p.getPosition();
        if (running) {
            if (distance(x, y, pos.getX(), pos.getY()) > maxMove) {
                return false;
            }
        }
        if (wrongPlace(x, y)) {
            return false;
        }

        p.setPosition(x, y);

        potionCheck(p);
        playersCheck(p);
        gameOnCheck();

        return true;
    }

    //@@ amis optimizaciaze vifiqro, tu 1 cellshi evri shedzleba mashin ar vici optimizacia rogor unda vqna Oo, axla ise wria ro sheidzleba, anu potionis randomsac yleze kidia, daje poionze gadaaebs.
    private void potionCheck(Player p) {
        Point2D.Double plPos = p.getPosition();
        Point2D.Double pot;
        for (int i = 0; i < potions.size(); i++) {
            pot = potions.get(i);
            if (distance(plPos.x + pRadius, plPos.y + pRadius, pot.x + potRadius, pot.y + potRadius) < pRadius + potRadius) {
                p.potionPlus();
                potions.remove(i);
                i--;
            }
        }
    }

    //@@ amovigo nameOnPlayer Mapidan tu prosta active false ? ? nika rogorc gadawyvets
    private void playersCheck(Player p) {
        Point2D.Double plPos = p.getPosition();
        Point2D.Double otherPlPos;

        Set<String> players = nameOnPlayer.keySet();

        for (String name : players) {
            Player otherP = nameOnPlayer.get(name);
            otherPlPos = otherP.getPosition();
            if (otherP.getActive() &&
                    distance(plPos.x + pRadius, plPos.y + pRadius, otherPlPos.x + pRadius, otherPlPos.y + pRadius) < pRadius + pRadius) {
                if (p.getPotNum() > otherP.getPotNum()) {
                    kickPlayer(p, otherP);
                } else if (p.getPotNum() < otherP.getPotNum()) {
                    kickPlayer(otherP, p);
                    break; // if player who made move kicked he cannot continue kicking anyone(or playing at all)
                }
            }
        }
    }

    private void kickPlayer(Player kicker, Player toKick) {
        toKick.setActive(false);
        kicker.setPotNum(kicker.getPotNum() + potForKick);
        activePlNum--;
    }

    private void gameOnCheck() {
        if (activePlNum < 2) {
            running = false;
            finishGame();
        }
    }

    /**
     * @param x
     * @param y
     * @return
     */
    private boolean wrongPlace(double x, double y) { // @@ aq armaq gatvaliswinebuli cellis shesadzlo ara-kvadratuloba
        int rowIndx = (int) (y / (wallWidth + cellWidth));
        if (rowIndx < 0 || y > rowIndx * (cellWidth + wallWidth) + cellWidth) {
            return true;
        }

        int colIndx = (int) (x / (wallWidth + cellWidth));
        if (colIndx < 0 || x > colIndx * (cellWidth + wallWidth) + cellWidth) {
            return true;
        }


//        if (pointInBounds(new Point(rowIndx - 1, colIndx))) {
//
//        } else if (pointInBounds(new Point(rowIndx, colIndx + 1))) {
//
//        } else if (pointInBounds(new Point(rowIndx + 1, colIndx))) {
//
//        } else if (pointInBounds(new Point(rowIndx, colIndx - 1))) {
//
//        }

        return false;
    }

    /**
     * @param cX
     * @param cY
     * @param rX
     * @param rY
     * @param rW
     * @param rH
     * @return
     */
    private boolean intersect(double cX, double cY, double rX, double rY, double rW, double rH) {
        double dx = Math.abs(cX - rX - rW / 2);
        double xDist = rW / 2 + pRadius;
        if (dx > xDist)
            return false;
        double dy = Math.abs(cY - rY - rH / 2);
        double yDist = rH / 2 + pRadius;
        if (dy > yDist)
            return false;
        if (dx <= rW / 2 || dy <= rH / 2)
            return true;
        double xCornerDist = dx - rW / 2;
        double yCornerDist = dy - rH / 2;
        double xCornerDistSq = xCornerDist * xCornerDist;
        double yCornerDistSq = yCornerDist * yCornerDist;
        double maxCornerDistSq = pRadius * pRadius;
        return xCornerDistSq + yCornerDistSq <= maxCornerDistSq;
    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    @Override
    public Collection<String> getPlayers() {
        return nameOnPlayer.keySet();
    }

    @Override
    public boolean gameOn() {
        return running;
    }

    @Override
    public void startGame() {
        if (!running) {
            timer = new Timer();
            incDistPeriodically();
            addPotPeriodically();
            running = true;
        }
    }

    private void incDistPeriodically() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                dist += plusDist;
                System.out.println("dist increased: " + dist);
            }
        }, 0, plusDistDelay);
    }

    private void addPotPeriodically() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                addPotion();
                System.out.println("Potion added");
            }
        }, 0, addPotDelay);
    }

    private void addPotion() {
        int randRow = rand.nextInt(numRows - 1);
        int randCol = rand.nextInt(numCols - 1);

        double rXInCell = randDouble(0, cellWidth - 2 * potRadius);
        double rYInCell = randDouble(potRadius, cellHeight - 2 * potRadius);

        potions.add(new Point2D.Double((cellWidth + wallWidth) * randCol + rXInCell,
                (cellHeight + wallWidth) * randRow + rYInCell));
    }

    /**
     * generates random value between two values [min, max), uniformly distributed.
     *
     * @param min generated value will not be lower
     * @param max generated value will not be higher
     * @return return double uniformly distributed in interval: [min, max)
     */
    private double randDouble(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    @Override
    public void finishGame() {
        if (running) {
            timer.cancel();
            running = false;
        }
    }
}