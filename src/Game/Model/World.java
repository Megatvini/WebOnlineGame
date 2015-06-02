package Game.Model;

import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

//@@ rezos vutxra rom worldis(mapis) 0, 0 aris charchos gareshe nawilis zeda-arcxena kutxeshi da ara charchoiana


/**
 * Created by SHAKO on 30-May-15.
 */
public class World implements iWorld {

    public static int MAX_PLAYERS = 4;

    private double wallWidth;
    private double corridorWidth;
    private PlaneMaze pm;
    private ArrayList<String> playerNames;
    private double pRadius;
    private double maxMove;
    private double dist;
    private double plusDist;
    private long plusDistDelay;
    private int potionNum;
    private double potRadius;
    private long addPotDelay;
    private int potForKick;
    private boolean gameOn;

    private double width;
    private double height;
    private int numRows;
    private int numCols;
    private Map<String, Player> nameOnPlayer;
    private ArrayList<Point2D.Double> potions;

    private Timer timer;
    private Random rand;

    /**
     * construct game object. It has two states on and off, represented with gameOn
     * variable, iff game is on: players cannot move from old distance on too far new distances, request
     * will be just ignored, player will be on same place; cannot add new players; cannot start game(again).
     * @param wallWidth width of maze wall, distance between, horizontal or vertical, adjacent corridors
     * @param corridorWidth width of horizontal or vertical corridor
     * @param pm abstract representation of maze, represents some maze and we can check where are and where are not walls
     * @param players names of players
     * @param pRadius radius of circle(s) representing player(s)
     * @param maxMove maximum length in pixels player can move from older location if game is on
     * @param dist minimum distance in pixels player can approach another player without risk of kicking out on of them
     * @param plusDist size in pixels to increase dist variable in every certain amount of time, if game is on
     * @param plusDistDelay length of time period in milliseconds to increase dist with plusDist in. not absolutely real time
     * @param potionNum number of potions to appear before game starts on random places
     * @param potRadius radius of circle(s) representing potion(s)
     * @param addPotDelay length of time period in milliseconds to add new potion at random place in. not absolutely real time
     * @param potForKick number of potions player receives if kicks one of other players
     * @param gameOn represents if is on or not. if true passed game will at the end of constructor.
     */
    public World(int numRows, int numCols, PlaneMaze pm,  double wallWidth, double corridorWidth, ArrayList<String> players, double pRadius, double maxMove, double dist, double plusDist, long plusDistDelay, int potionNum, double potRadius, long addPotDelay, int potForKick, boolean gameOn) {

        gameOn = false; // until world constructor finishes clearly game is not on
        this.wallWidth = wallWidth;
        this.corridorWidth = corridorWidth;
        this.pm = pm;

        numRows = pm.numRows();
        numCols = pm.numCols();

        this.width = numCols * corridorWidth + (numCols - 1) * wallWidth;
        this.height = numRows * corridorWidth + (numRows - 1) * wallWidth;

        playerNames = players;
        this.pRadius = pRadius;
        this.maxMove = maxMove;

        nameOnPlayer = new HashMap<String, Player>();

        for (int i = 0; i < playerNames.size(); i++) {
            addPlayer(playerNames.get(i));
        }

        this.dist = dist;
        this.plusDist = plusDist;
        this.plusDistDelay = plusDistDelay;

        potions = new ArrayList<Point2D.Double>();

        this.potionNum = potionNum; // @@ potions da aseve yvela array saxelebi da ase shmdeg tu kide ramea copy clone shit...
        this.addPotDelay = addPotDelay;
        this.potRadius = potRadius;
        this.potForKick = potForKick;
        this.gameOn = gameOn;

        if (gameOn) {
            startGame();
        }
    }

    @Override
    public boolean addPlayer(String playerName) {
        if (!gameOn || playerNames.size() > MAX_PLAYERS || playerNames.contains(playerName)) {
            Player p = null;
            switch (playerNames.size()) {
                case 0:
                    p = new Player((corridorWidth - 2 * pRadius) / 2, (corridorWidth - 2 * pRadius) / 2, 2 * pRadius, true);
                    break;
                case 1:
                    p = new Player((numCols - 1) * corridorWidth + (numCols - 1) * wallWidth + (corridorWidth - 2 * pRadius) / 2,
                            (corridorWidth - 2 * pRadius) / 2,
                            pRadius,
                            true);
                    break;
                case 2:
                    p = new Player((numCols - 1) * corridorWidth + (numCols - 1) * wallWidth + (corridorWidth - 2 * pRadius) / 2,
                            (numRows - 1) * corridorWidth + (numRows - 1) * wallWidth + (corridorWidth - 2 * pRadius) / 2,
                            pRadius,
                            true);
                    break;
                case 3:
                    p = new Player((corridorWidth - 2 * pRadius) / 2,
                            (numRows - 1) * corridorWidth + (numRows - 1) * wallWidth + (corridorWidth - 2 * pRadius) / 2,
                            pRadius,
                            true);
                    break;
                default:
                    return false;
            }
            playerNames.add(playerName);
            nameOnPlayer.put(playerName, p);
            return true;
        }
        return false;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public int numRows() {
        return numRows;
    }

    @Override
    public int numCols() {
        return numCols;
    }

    @Override
    public PlaneMaze getMaze() {
        return pm;
    }

    @Override //@@ xom ar joiba misaarts rom aigebs imis stringad gadaqcevisas lockaves rameebs(safiqria aq ras)
    public Object[] getState() { //@@ misamartis copy ar unda davubruno savaraudod, elementis copy minda, mara elem-copy didi operaciaa duuh
        return new Object[]{nameOnPlayer, potions, dist};
    }



    @Override
    public int numberOfPlayers() {
        return playerNames.size();
    }

    @Override //@@ codis gameoreba mivxedo setPlayerLocation-tan aris
    public boolean playerMove(String playerName, double dx, double dy) {
        Player p = nameOnPlayer.get(playerName);
        Point2D.Double pos = p.getPosition();
        if (gameOn) {
            if (distance(dx, dy, 0, 0) > maxMove) {
                return false;
            }
        }
        if  (wrongPlace(pos.x + dx, pos.y + dx)) {
            return false;
        }
        p.setPosition(pos.x + dx, pos.y + dy);

        potionCheck(p);
        playersCheck(p);

        return true;
    }

    @Override
    public boolean setPlayerCoordinates(String playerName, double x, double y) {
        Player p = nameOnPlayer.get(playerName);
        Point2D.Double pos = p.getPosition();
        if (gameOn) {
            if (distance(x, y, pos.getX(), pos.getY()) > maxMove) {
                return false;
            }
        }
        if  (wrongPlace(x, y)) {
            return false;
        }

        p.setPosition(x, y);

        potionCheck(p);
        playersCheck(p);

        return true;
    }

    //@@ amis optimizaciaze vifiqro, tu 1 cellshi evri shedzleba mashin ar vici optimizacia rogor unda vqna Oo, axla ise wria ro sheidzleba, anu potionis randomsac yleze kidia, daje poionze gadaaebs.
    private void potionCheck(Player p) {
        Point2D.Double plPos = p.getPosition();
        Point2D.Double pot;
        for (int i = 0; i < potions.size(); i++) {
            pot = potions.get(i);
            if (distance(plPos.x, plPos.y, pot.x, pot.y) < pRadius + potRadius) {
                p.potionPlus();
                potions.remove(i);
                i--;
            }
        }
    }

    //@@ amovigo nameOnPlayer Mapidan tu prosta active false ? ? nika rogorc gadawyvets
    private void playersCheck(Player p) {
        Point2D.Double plPos = p.getPosition();
        Player otherPl;
        Point2D.Double otherPlPos ;
        for (int i = 0; i < playerNames.size(); i++) {
            otherPl = nameOnPlayer.get(playerNames.get(i));
            otherPlPos = otherPl.getPosition();
            if (otherPl.getActive() && distance(plPos.x, plPos.y, otherPlPos.x, otherPlPos.y) < pRadius + pRadius) {
                if (p.getPotNum() > otherPl.getPotNum()) {
                    otherPl.setActive(false);
                    p.setPotNum(p.getPotNum() + potForKick);
                } else if (p.getPotNum() < otherPl.getPotNum()) {
                    p.setActive(false);
                    otherPl.setPotNum(otherPl.getPotNum() + potForKick);
                    break; // if player who made move kicked he cannot continue kicking anyone(or playing at all)
                }
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private boolean wrongPlace(double x, double y) {
        int rowIndx = (int)(y / (wallWidth + corridorWidth));
        if (rowIndx < 0 || y > rowIndx * (corridorWidth + wallWidth) + corridorWidth) {
            return true;
        }

        int colIndx = (int)(x / (wallWidth + corridorWidth));
        if (colIndx < 0 || x > colIndx * (corridorWidth + wallWidth) + corridorWidth) {
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
     *
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
     *
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
    public List<String> getPlayers() {
        return playerNames;
    }

    @Override
    public boolean gameOn() {
        return false;
    }

    @Override
    public void startGame() {
        if (!gameOn) {
            timer = new Timer();
            rand = new Random();
            increaseDist();
            addPotions();
            gameOn = true;
        }
    }

    private void increaseDist() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                dist += plusDist;
            }
        }, 0, plusDistDelay);
    }

    private void addPotions() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int randRow = rand.nextInt(numRows - 1);
                int randCol = rand.nextInt(numCols - 1);

                double rXInCell = randDouble(potRadius, corridorWidth - potRadius);
                double rYInCell = randDouble(potRadius, corridorWidth - potRadius);

                potions.add(new Point2D.Double((corridorWidth + wallWidth) * randCol + rXInCell,
                        (corridorWidth + wallWidth) * randRow + rYInCell));
            }
        }, 0, addPotDelay);
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

    @Override
    public void finishGame() {
        timer.cancel();
    }


    public class Player {

        private  double x;
        private double y;

        private double radius;

        private int potionNum;

        private boolean active;

        private Player(double x, double y, double radius, boolean active) {
            this.x = x;
            this.y = y;

            this.radius = radius;

            this.active = active;
        }

        private Point2D.Double getPosition() {
            return new Point2D.Double(x, y);
        }

        private void setPosition(double x, double y) {
            this.x = x;
            this.y = y;
        }

        private void setPosition(Point2D.Double loc) {
            x = loc.x;
            y = loc.y;
        }

        private void potionPlus() {
            potionNum++;
        }

        private void setPotNum(int potNum) {
            potionNum = potNum;
        }

        private int getPotNum() {
            return potionNum;
        }

        private void setActive(boolean active){
            this.active = active;
        }

        private boolean getActive() {
            return active;
        }

    }




}































