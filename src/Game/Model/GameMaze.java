package Game.Model;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

//@@ sinqronizaciaq davikide ro davwere mere vifiqreb
//@@ it will override player

/**
 * Created by SHAKO on 18-Jun-15.
 */
public class GameMaze extends PlaneMaze {

    // number of corners of game maze
    private static final int CORNER_NUM = 4;

    private static Random rand = new Random();

    private double width;
    private double height;
    private double wallWidth;
    private double pRadius;
    private double maxMove;
    private double dist;
    private double potRadius;
    private double cellWidth;
    private double cellHeight;

    private Map<String, Point2D.Double> nameOnPos;
    private Set<Point2D.Double> potions;

    // number of player added at one corners of game world
    private int playersAtCorner;

    public GameMaze(int numRows, int numCols, Configuration config) {
        super(numRows, numCols);
        readConfig(config);
        nameOnPos = new HashMap<>();
        potions = new HashSet<>();
        playersAtCorner = 0;
    }

    private void readConfig(Configuration config) {
        width = config.getWidth();
        height = config.getHeight();
        wallWidth = config.getWallWidth();
        pRadius = config.getPRadius();
        maxMove = config.getMaxMove();
        dist = config.getStartDist();
        potRadius = config.getPotRadius();
        cellWidth = config.getCellWidth();
        cellHeight = config.getCellHeight();
    }


    public boolean addPlayerAtCorner(String name) {
        for (int i = playersAtCorner; i < CORNER_NUM; i++) {
            if (tryPlayerInCell(name, getCornerCell(i))) {
                playersAtCorner++;
                return true;
            }
        }
        return false;
    }

    public synchronized boolean addPlayerAtRandom(String name) {
        // TODO method code bitch !
        return false;
    }

    public boolean addPlayerInCell(String name, Cell c) {
        if (tryPlayerInCell(name, c)) {
            if (isCorner(c)) {
                playersAtCorner++;
            }
            return true;
        }
        return false;
    }

    public Point2D.Double removePlayer(String name) {
        return nameOnPos.remove(name);
    }

    public Point2D.Double addPotAtRandom(boolean conflictAllowed) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Point2D.Double pot = randOval(potRadius);
            if (!potions.contains(pot)
                    && (conflictAllowed || !ovalConflicts(pot, potRadius, nameOnPos.values(), pRadius))) {
                potions.add(pot);
                return pot;
            }
        }
        return null;
    }

    public Point2D.Double addPotInCell(Cell c, boolean conflictAllowed) {
        if (c.row < 0 || c.row > numRows - 1 ||
                c.col < 0 || c.col > numCols - 1) {
            throw new RuntimeException("Cell is out of bounds!");
        }
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Point2D.Double pot = randOvalInCell(c, potRadius);
            if (!potions.contains(pot)
                    && (conflictAllowed || !ovalConflicts(pot, potRadius, nameOnPos.values(), pRadius))) {
                potions.add(pot);
                return pot;
            }
        }
        return null;
    }

    public boolean removePot(Point2D.Double pot) {
        return potions.remove(pot);
    }

    public boolean longMove(String name, double x, double y) {
        Point2D.Double plPos = nameOnPos.get(name);
        return distance(plPos.x, plPos.y, x, y) > maxMove;
    }

    public boolean collideWall(String name) {
        return false;
    }

    public double getDist() {
        return dist;
    }

    public void increaseDist(double plusD) {
        dist += plusD;
    }

    public void plusPlayerPos(String name, double dx, double dy) {
        Point2D.Double pos = nameOnPos.get(name);
        nameOnPos.get(name).setLocation(pos.x + dx, pos.y + dy);
    }

    public Point2D.Double getPlPosition(String name) {
        Point2D.Double pos = nameOnPos.get(name);
        return new Point2D.Double(pos.x, pos.y);
    }

    public void setPlayerPos(String name, double x, double y) {
        nameOnPos.get(name).setLocation(x, y);
    }

    public Set<Point2D.Double> getPotions() {
        return Collections.unmodifiableSet(potions);
    }

    /* after some action: detect collisions methods */

    public Collection<Point2D.Double> collidedPotions(String name) {
        Collection<Point2D.Double> collidedPots = new ArrayList<>();
        Point2D.Double playerPos = nameOnPos.get(name);
        Iterator<Point2D.Double> potsIt = potions.iterator();
        while(potsIt.hasNext()) {
            Point2D.Double nextPot = potsIt.next();
            if (ovalsIntersect(playerPos, pRadius, nextPot, potRadius)) {
                collidedPots.add(nextPot);
            }
        }
        return collidedPots;
    }

    public Collection<String> collidedPlayers(String name) {
        Collection<String> collidedPls = new ArrayList<>();
        Point2D.Double playerPos = nameOnPos.get(name);
        Iterator<String> plsIt = nameOnPos.keySet().iterator();
        while(plsIt.hasNext()) {
            String nextPl = plsIt.next();
            if (!nextPl.equals(name)
                    && ovalsIntersect(playerPos, dist / 2, nameOnPos.get(nextPl), dist / 2)) {
                collidedPls.add(nextPl);
            }
        }
        return collidedPls;
    }

    public Collection<String> collidedPlayers(Point2D.Double pot) {
        Collection<String> collidedPls = new ArrayList<>();
        Iterator<String> plsIt = nameOnPos.keySet().iterator();
        while(plsIt.hasNext()) {
            String nextPl = plsIt.next();
            if (ovalsIntersect(pot, potRadius, nameOnPos.get(nextPl), pRadius)) {
                collidedPls.add(nextPl);
            }
        }
        return collidedPls;
    }

    public JsonObjectBuilder configJsonBuilder() {
        JsonObjectBuilder configJson = Json.createObjectBuilder();
        configJson.add("width", width)
                .add("height", height)
                .add("wallWidth", wallWidth)
                .add("pRadius", pRadius)
                .add("potRadius", potRadius);
        return configJson;
    }

    // private methods

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

    private boolean tryPlayerInCell(String name, Cell c) {
        if (c.row < 0 || c.row > numRows - 1 ||
                c.col < 0 || c.col > numCols - 1) {
            throw new RuntimeException("Cell is out of bounds!");
        }
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Point2D.Double pos = randOvalInCell(c, pRadius);
            if (!ovalConflicts(pos, dist, nameOnPos.values(), dist) &&
                    !ovalConflicts(pos, pRadius, potions, potRadius)) {
                nameOnPos.put(name, pos);
                return true;
            }
        }
        return false;
    }

    private Point2D.Double randOval(double radius) {
        int randRow = rand.nextInt(numRows);
        int randCol = rand.nextInt(numCols);
        return randOvalInCell(new Cell(randRow, randCol), radius);
    }

    private Point2D.Double randOvalInCell(Cell c, double radius) {
        double rXInCell = randDouble(0, cellWidth - 2 * radius);
        double rYInCell = randDouble(0, cellHeight - 2 * radius);

        return new Point2D.Double((cellWidth + wallWidth) * c.col + rXInCell,
                (cellHeight + wallWidth) * c.row + rYInCell);
    }

    private double randDouble(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    private boolean ovalConflicts(Point2D.Double pos, double r, Collection<Point2D.Double> ovals, double ovalsR) {
        return ovals.stream().anyMatch(o -> ovalsIntersect(pos, r, o, ovalsR));
    }

    private boolean ovalsIntersect(Point2D.Double o1, double r1, Point2D.Double o2, double r2) {
        return distance(o1.x + r1, o1.y + r1, o2.x + r2, o2.y + r2) < r1 + r2;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private boolean isCorner(Cell c) {
        return (c.row == 0 || c.row == numRows - 1) && (c.col == 0 || c.col == numCols - 1);
    }

     /* collision methods */

//    private boolean wrongPlace(Double x, Double y) {
//        if (x < 0 || x + 2 * pRadius > width || y < 0 || y + 2 * pRadius > height) { return true; }
//
//        double cX = x + pRadius;
//        double cY = y + pRadius;
//        double rowIndx = cY / (cellHeight + wallWidth);
//        double colIndx = cX / (cellWidth + wallWidth);
//        boolean collideHor = false;
//        boolean collideVer = false;
//        if (cY > rowIndx * (cellHeight + wallWidth) + cellHeight) { collideHor = true; }
//        if (cX > colIndx * (cellWidth + wallWidth) + cellWidth) { collideVer = true; }
//
//        if (collideHor && collideVer) {
//
//        } else if (collideHor) {
//
//        } else if (collideVer) {
//
//        } else {
//
//        }
//
////        if (collideAround(x, y, new Cell(rowIndx, colIndx))) {
////            return true;
////        }
//
//        return false;
//    }
//
////    private boolean collideAround(double x, double y, Cell c) {
////        for (int i = -1; i < 2; i++) {
////            for (int j = -1; j < 2; j++) {
////                Cell neighbour = new Cell(c.row + i, c.col + j);
////                if (c.row == neighbour.row ^ c.col == neighbour.col) {
////                    if (!pm.cellInBounds(neighbour) || pm.isWall(c, neighbour)) {
////                        if (intersect(x + pRadius, y + pRadius, pRadius, getLongRect(c, neighbour))) {
////                            return true;
////                        }
////                    }
////                } else {
////                    if (c.row != neighbour.row || c.col != neighbour.col) {
////                        if (!pm.cellInBounds(neighbour) || isSmallRect(c, neighbour)) {
////                            if (intersect(x + pRadius, y + pRadius, pRadius, getSmallRect(c, neighbour))) {
////                                return true;
////                            }
////                        }
////                    }
////                }
////            }
////        }
////        return false;
////    }
//
//    /*
//     * @@ have to be adjacent
//     */
//    private Rectangle2D.Double getLongRect(Cell c1, Cell c2) {
//        Rectangle2D.Double r;
//        if (c1.row == c2.row) {
//            int maxRow = Math.max(c1.row, c2.row);
//            r = new Rectangle2D.Double(cellWidth * maxRow + wallWidth * (maxRow - 1),
//                    (cellHeight + wallWidth) * c1.row,
//                    wallWidth,
//                    cellHeight);
//
//        } else {
//            int maxCol = Math.max(c1.col, c2.col);
//            r = new Rectangle2D.Double((cellWidth + wallWidth) * c1.col,
//                    cellHeight * maxCol + wallWidth * (maxCol - 1),
//                    cellWidth,
//                    wallWidth);
//
//        }
//        return r;
//    }
//
//    /**
//     * checks if given circumference and rectangle intersect each other
//     * @param cX centre x coordinate of  circumference
//     * @param cY centre y coordinate of  circumference
//     * @param rad radius of circumference
//     * @param rect rectangle to check collision with
//     * @return true - iff given circumference and rectangle intersects each other, false otherwise
//     */
//    private boolean intersect(double cX, double cY, double rad, Rectangle2D.Double rect) {
//        double dx = Math.abs(cX - rect.x - rect.width / 2);
//        double xDist = rect.width / 2 + rad;
//        if (dx > xDist)
//            return false;
//        double dy = Math.abs(cY - rect.y - rect.height / 2);
//        double yDist = rect.height / 2 + rad;
//        if (dy > yDist)
//            return false;
//        if (dx <= rect.width / 2 || dy <= rect.height / 2)
//            return true;
//        double xCornerDist = dx - rect.width / 2;
//        double yCornerDist = dy - rect.height / 2;
//        double xCornerDistSq = xCornerDist * xCornerDist;
//        double yCornerDistSq = yCornerDist * yCornerDist;
//        double maxCornerDistSq = rad * rad;
//        return xCornerDistSq + yCornerDistSq <= maxCornerDistSq;
//    }
//
//    /*
//     * @@ must not be out of bounds non of them
//     * @@ have to be diagonally adjacent
//     */
////    private boolean isSmallRect(Cell c1, Cell c2) {
////        int minRow = Math.min(c1.row, c2.row);
////        int minCol = Math.min(c1.col, c2.col);
////        int maxRow = Math.max(c1.row, c2.row);
////        int maxCol = Math.max(c1.col, c2.col);
////
////        Cell upLeft = new Cell(minRow, minCol);
////        Cell upRight = new Cell(minRow, minCol + 1);
////        Cell downRight = new Cell(maxRow, maxCol);
////        Cell downLeft = new Cell(maxRow, maxCol - 1);
////
////
////        if (pm.isWall(upLeft, upRight)) {
////            return true;
////        } else if (pm.isWall(upRight, downRight)) {
////            return true;
////        } else if (pm.isWall(downRight, downLeft)) {
////            return true;
////        } else if (pm.isWall(downLeft, upLeft)) {
////            return true;
////        }
////        return false;
////    }
//
//    /*
//     * @@ have to be diagonally adjacent
//     */
//    private Rectangle2D.Double getSmallRect(Cell c1, Cell c2) {
//        Rectangle2D.Double r = new Rectangle2D.Double();
//        r.width = wallWidth;
//        r.height = wallWidth;
//
//        int minRow = Math.min(c1.row, c2.row);
//        int minCol = Math.min(c1.col, c2.col);
//
//        r.x = cellWidth * (minCol + 1) + wallWidth * minCol;
//        r.y = cellHeight * (minRow + 1) + wallWidth * minRow;
//        return r;
//    }
}









    /**
     * Gets cell for index-th corner of maze, 0th is up-left,
     * indexing is clockwise, index must be in [0, 3] interval,
     * if not runtime exeption will be thrown.
     * @param index index of corner cell to be returned
     * @return cell for index-th corner of maze
     */
//private Cell getCornerCell(int index) {

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
//private boolean tryPlayerInCell(Player p, Cell c) {

/**
 * Checks if given player conflicts with one of players(i.e.
 * it is near one of active player than given distance and they
 * have different count of potions) or with one of potions(i.e. it
 * intersects one of potion). Given player have to be active, otherwise
 * this method is redundant(false returned immediately if not active)
 //* @param player player to check conflict for
 * @return true iff player is active and conflicts with players or potions
 */
//private boolean playerConflicts(Player player) {


/**
 * Checks if given player conflicts with one of players(i.e.
 * it is near one of active player than given distance and they have
 * different count of potions). Given player have to be active, otherwise
 * this method is redundant(false returned immediately if not active)
 //* @param player player to check conflict for
 * @return true iff player is active and conflicts with players or potions
 */
//private boolean plConflictPl(Player player) {

/**
 * Checks if given player conflicts with one of potions(i.e. it
 * intersects one of potion). Given player have to be active, otherwise
 * this method is redundant(false returned immediately if not active)
 //* @param player player to check conflict for
 * @return true iff player is active and conflicts with players or potions
 */
//private boolean plConflictPot(Player player) {

/**
 * Tries to add potion in random cell, on random position. If potion
 * with some position conflicts with one of players(i.e. potion intersects with
 * one of players) tries another cell xor position.
 * @return true iff potion added
 */
//public synchronized boolean addPotAtRandom(boolean cornerAllowed) {


/**
 * checks if given cell is one of corners of game map
 * @param c cell to check if it is corner cell
 * @return true iff given cell is one of corner cells of game map
 */
//private boolean isCorner(Cell c) {

    /**
     * Tries to add potion in given cell, on random position. If potion
     * with some position conflicts with one of players(i.e. potion intersects with
     * one of players) tries another position. If cell is out of bounds of game map
     * proper runtime exception will be thrown.
     * @param c cell to add new potion in
     * @return true iff potion added
     */
    //public synchronized boolean addPotInCell(Cell c) {
