package Game.Model;

import Core.Bean.Game;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by SHAKO on 18-Jun-15.
 * GameMaze class, extend from PlaneMaze, its plane maze and plus
 * functional, mathematics for game, i.e. add players potions, check player collisions
 * with potions, other players and walls.
 */
public class GameMaze extends PlaneMaze {

    // number of corners of game maze
    private static final int CORNER_NUM = 4;

    // instance to generate pseudo random things(int, double, boolean ...)
    private static Random rand = new Random();

    // variables to read from configuration object
    private double width;
    private double height;
    private double wallWidth;
    private double pRadius;
    private double maxMove;
    private double dist;
    private double potRadius;
    private double cellWidth;
    private double cellHeight;

    // player saving added player(string name) as key and its current position as value
    private Map<String, Point2D.Double> nameOnPos;

    // set of players removed
    private Set<String> removedPlayers;

    // potions currently in game
    private Set<Point2D.Double> potions;

    // number of player added at one corners of game world
    private int playersAtCorner;

    /**
     * constuctor for testing
     */
    public GameMaze(Configuration config, Map<String, Point2D.Double> nameOnPos, Set<Point2D.Double> potions) {
        this(config);
        this.nameOnPos = nameOnPos;
        this.potions = potions;
    }

    /**
     * Constructor getting configuration object, containing game parameters,
     * creates game maze object according config dealing with mathematics of game,
     * like, coordinates, collision etc.
     * @param config configuration object containing game parameters
     */
    public GameMaze(Configuration config) {
        super(config.getNumRows(), config.getNumCols());
        readConfig(config);
        nameOnPos = Collections.synchronizedMap(new HashMap<>());
        removedPlayers = Collections.synchronizedSet(new HashSet<>());
        potions = Collections.synchronizedSet(new HashSet<>());
        playersAtCorner = 0;
    }

    /**
     * Reads variables from given configuration file into instance
     * variables
     * @param config configuration file describing game parameters
     */
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

    /**
     * Tries to add new player with given name at one of corner, on some random part of cell.
     * First tries up-left corner, then tries next clockwise. If player conflicts with
     * one of players(i.e. it is near to one of player then determined distance) or potion
     * (i.e. it overlaps one of potions), tries next corner, if does not finds proper corner
     * does not adds new player. Considers only start positions of player, given by one of player
     * addition methods, even if you move player around. Cannot add two player with same name
     * @param name name of new player to add
     * @return true iff player added
     */
    public synchronized boolean addPlayerAtCorner(String name) {
        if (nameOnPos.containsKey(name)) return false;
        for (int i = playersAtCorner; i < CORNER_NUM; i++) {
            if (tryPlayerInCell(name, getCornerCell(i))) {
                playersAtCorner++;
                return true;
            }
        }
        return false;
    }

    /**
     * Adds player at some random position so that it does not conflicts with
     * one of players(i.e. it is near to one of player then determined distance) or potion
     * (i.e. it overlaps one of potions). Cannot add two player with same name
     * @param name name of new player
     * @return true if player added
     */
    public synchronized boolean addPlayerAtRandom(String name) {
        if (nameOnPos.containsKey(name)) return false;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Cell c = new Cell(i, j);
                if (tryPlayerInCell(name, c)) {
                    if (isCorner(c)) playersAtCorner++;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds player at some random position in given cell so that it does not conflicts with
     * one of players(i.e. it is near to one of player then determined distance) or potion
     * (i.e. it overlaps one of potions). Cannot add two player with same name
     * @param name name of new player
     * @return true if player added
     * @throws IllegalArgumentException if cell is out of bounds, i.e. row or column is less than
     * zero or row or column is more than number of rows minus one, number of columns minus one respectively
     */
    public synchronized boolean addPlayerInCell(String name, Cell c) {
        if (nameOnPos.containsKey(name)) return false;
        if (c.row < 0 || c.row > numRows - 1 ||
                c.col < 0 || c.col > numCols - 1) {
            throw new RuntimeException("Cell is out of bounds!");
        }
        if (tryPlayerInCell(name, c)) {
            if (isCorner(c)) playersAtCorner++;
            return true;
        }
        return false;
    }

    /**
     * Tries to add new potion at some random position so that it does not collide with any wall,
     * and if conflict is not allowed it also does not intersects any player.
     * @param conflictAllowed tells if conflict wth player allowed or not, it new potion can collide player
     * @return new potion place added or null if place not found
     */
    public synchronized Point2D.Double addPotAtRandom(boolean conflictAllowed) {
        for (int i = 0; i < 2 * width * height; i++) {
            Point2D.Double pot = randOval(potRadius);
            if (!potions.contains(pot)
                    && (conflictAllowed || !ovalConflicts(pot, potRadius, nameOnPos.values(), pRadius))) {
                potions.add(pot);
                return pot;
            }
        }
        return null;
    }

    /**
     * Tries to add new potion at some random position in given cell so that it does not collide with
     * any wall, and if conflict is not allowed it also does not intersects any player.
     * @param c cell to add new potion in
     * @param conflictAllowed tells if conflict wth player allowed or not, it new potion can collide player
     * @return new potion place added or null if place not found
     * @throws IllegalArgumentException if cell is out of bounds, i.e. row or column is less than
     * zero or row or column is more than number of rows minus one, number of columns minus one respectively
     */
    public synchronized Point2D.Double addPotInCell(Cell c, boolean conflictAllowed) {
        if (c.row < 0 || c.row > numRows - 1 ||
                c.col < 0 || c.col > numCols - 1) {
            throw new RuntimeException("Cell is out of bounds!");
        }
        for (int i = 0; i < cellWidth * cellHeight; i++) {
            Point2D.Double pot = randOvalInCell(c, potRadius);
            if (!potions.contains(pot)
                    && (conflictAllowed || !ovalConflicts(pot, potRadius, nameOnPos.values(), pRadius))) {
                potions.add(pot);
                return pot;
            }
        }
        return null;
    }


    /**
     * Removes player with given name from GameMaze.
     * @param name name of player to remove
     * @return removed player's position or null if there was no player
     * with given name(or there was but already removed)
     */
    public synchronized Point2D.Double removePlayer(String name) {
        if (nameOnPos.containsKey(name) && !removedPlayers.contains(name)) removedPlayers.add(name);
        else return null;
        return nameOnPos.get(name);
    }

    /**
     * Removes given potion from game maze
     * @param pot potion to remove from game maze
     */
    public void removePot(Point2D.Double pot) {
        potions.remove(pot);
    }

    /**
     * Tells if moving player to given position is not allowed due to length of move.
     * @param name name of player to check move for
     * @param x x position of player to check move for
     * @param y y position of player to check move for
     * @return true if move given player to give position is long then determined length
     */
    public synchronized boolean longMove(String name, double x, double y) {
        Point2D.Double plPos = nameOnPos.get(name);
        return distance(plPos.x, plPos.y, x, y) > maxMove;
    }

    /**
     * checks if given player on given position will collide any walls
     * @param radius radius of oval to check collide with walls for
     * @param x x coordinate to check collision on
     * @param y y coordinate to check collision on
     * @return true iff given player on given possition collides with any of walls
     */
    public boolean collideWall(double radius, double x, double y) {
//        if (x < 0 || x + 2 * pRadius > width || y < 0 || y + 2 * pRadius > height) { return true; }
//
//        double cX = x + pRadius;
//        double cY = y + pRadius;
//        int rowIndx = (int)(cY / (cellHeight + wallWidth));
//        int colIndx = (int)(cX / (cellWidth + wallWidth));
//        boolean collideHor = false;
//        boolean collideVer = false;
//        if (cY > rowIndx * (cellHeight + wallWidth) + cellHeight) { collideHor = true; }
//        if (cX > colIndx * (cellWidth + wallWidth) + cellWidth) { collideVer = true; }
//
//
//        if (collideHor && collideVer) {
//            return isSquare(rowIndx, colIndx);
//        } else if (collideHor) {
//            if (isWall(rowIndx, colIndx, rowIndx + 1, colIndx)) {
//                return true;
//            } else {
//                if () ...''
//                return isSquare();
//            }
//        } else if (collideVer) {
//            if (isWall(rowIndx, colIndx, rowIndx, colIndx + 1)) {
//                return true;
//            } else {
//                if ()...
//                return isSquare();
//            }
//        } else {
//            boolean touchesUp =;
//            if (touchesUp) {
//
//            } else if () {
//
//            } else if () {
//
//            } else {
//
//            }
//
//        }

        return false;
    }

    /**
     * Returns minimum distance players approach each others
     * @return minimum distance players approach each others
     */
    public synchronized double getDist() {
        return dist;
    }

    /**
     * Increases minimum distance players approach each others
     * @param plusD value to add to minimum distance players approach each others
     */
    public synchronized void increaseDist(double plusD) {
        dist += plusD;
    }

    /**
     * Addeds given values to given players current position
     * @param name player name to increase position
     * @param dx value to increase players x coordinat
     * @param dy value to increase players y coordinat
     */
    public synchronized void plusPlayerPos(String name, double dx, double dy) {
        Point2D.Double pos = nameOnPos.get(name);
        nameOnPos.get(name).setLocation(pos.x + dx, pos.y + dy);
    }

    /**
     * Returns position of player with given name
     * @param name name of player to return position of
     * @return given player's position
     */
    public synchronized Point2D.Double getPlPosition(String name) {
        Point2D.Double pos = nameOnPos.get(name);
        return new Point2D.Double(pos.x, pos.y);
    }

    /**
     * Sets player with given name on given position
     * @param name name of player to set position
     * @param x x coordinate to put player on
     * @param y y coordinate to put player on
     */
    public synchronized void setPlayerPos(String name, double x, double y) {
        nameOnPos.get(name).setLocation(x, y);
    }

    /**
     * Returns potions
     * @return potions
     */
    public synchronized Set<Point2D.Double> getPotions() {
        return Collections.unmodifiableSet(potions);
    }

    /* after some action: detect collisions methods */

    /**
     * Returns potions which have collision with player with given name
     * @param name name of player to check collision with potions for
     * @return potions which have collision with player with given name
     */
    public synchronized Collection<Point2D.Double> collidedPotions(String name) {
        Collection<Point2D.Double> collidedPots = new ArrayList<>();
        Point2D.Double playerPos = nameOnPos.get(name);
        synchronized (potions) {
            Iterator<Point2D.Double> potsIt = potions.iterator();
            while(potsIt.hasNext()) {
                Point2D.Double nextPot = potsIt.next();
                if (ovalsIntersect(playerPos, pRadius, nextPot, potRadius)) {
                    collidedPots.add(nextPot);
                }
            }
        }
        return Collections.unmodifiableCollection(collidedPots);
    }

    /**
     * Returns players which are near than determined distance to player with given name
     * @param name name of player to check distance with players for
     * @return players which are near than determined distance to player with given name
     */
    public synchronized Collection<String> collidedPlayers(String name) {
        Collection<String> collidedPls = new ArrayList<>();
        Point2D.Double playerPos = nameOnPos.get(name);
        synchronized (nameOnPos) {
            Iterator<String> plsIt = nameOnPos.keySet().iterator();
            while (plsIt.hasNext()) {
                String nextPl = plsIt.next();
                if (!nextPl.equals(name)
                        && ovalsIntersect(playerPos, dist / 2, nameOnPos.get(nextPl), dist / 2)
                        && !removedPlayers.contains(nextPl)) {
                    collidedPls.add(nextPl);
                }
            }
        }
        return collidedPls;
    }

    /**
     * Returns players which have collision with given potion
     * @param pot potion to check collision with players for
     * @return players which have collision with given potion
     */
    public synchronized Collection<String> collidedPlayers(Point2D.Double pot) {
        Collection<String> collidedPls = new ArrayList<>();
        synchronized (nameOnPos) {
            Iterator<String> plsIt = nameOnPos.keySet().iterator();
            while(plsIt.hasNext()) {
                String nextPl = plsIt.next();
                if (ovalsIntersect(pot, potRadius, nameOnPos.get(nextPl), pRadius)) {
                    collidedPls.add(nextPl);
                }
            }
        }
        return collidedPls;
    }

    /**
     * Returns configuration info as JsonObjectBuilder of GameMaze
     * @return configuration info as JsonObjectBuilder of GameMaze
     */
    public synchronized JsonObjectBuilder configJsonBuilder() {
        JsonObjectBuilder configJson = Json.createObjectBuilder();
        configJson.add("width", width)
                .add("height", height)
                .add("wallWidth", wallWidth)
                .add("pRadius", pRadius)
                .add("potRadius", potRadius);
        return configJson;
    }

    // private methods

    /**
     * Returns one of corner cells, depended on given index, for 0th index returns
     * up-left corner then continues clockwise
     * @param index index of corner cell to returns
     * @return one of corner cells, depended on given index, for 0th index returns
     * up-left corner then continues clockwise
     * @throws IllegalArgumentException if given index is more then corner count minus one
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
                throw new IllegalArgumentException("Index must be in [0, 3] interval!");
        }
        return c;
    }

    /**
     * Tries to add player on given cell at some random place in that cell, not
     * colliding walls or potions and is not near other player than determined distance.
     * @param name name of new player
     * @param c cell to add new player on
     * @return true iff player added successfully
     * @throws IllegalArgumentException if cell is out of bounds, i.e. row or column is less than
     * zero or row or column is more than number of rows minus one, number of columns minus one respectively
     */
    private boolean tryPlayerInCell(String name, Cell c) {
        if (c.row < 0 || c.row > numRows - 1 ||
                c.col < 0 || c.col > numCols - 1) {
            throw new IllegalArgumentException("Cell is out of bounds!");
        }
        for (int i = 0; i < cellWidth * cellHeight; i++) {
            Point2D.Double pos = randOvalInCell(c, pRadius);
            if (!ovalConflicts(pos, dist / 2, nameOnPos.values(), dist / 2) &&
                    !ovalConflicts(pos, pRadius, potions, potRadius)) {
                nameOnPos.put(name, pos);
                return true;
            }
        }
        return false;
    }

    /**
     * Generates and returns random position(up left point of surrounding square) of oval
     * with given radius so that it does not intersects any walls, in facts its in one of cell
     * @param radius radius of oval to generate
     * @return random position(up left point of surrounding square) of oval with given radius so that
     * it does not intersects any walls, in facts its in one of cell
     */
    private Point2D.Double randOval(double radius) {
        int randRow = rand.nextInt(numRows);
        int randCol = rand.nextInt(numCols);
        return randOvalInCell(new Cell(randRow, randCol), radius);
    }

    /**
     * Generates and returns random position(up left point of surrounding square) in given cell
     * of oval with given radius so that it does not intersects any walls
     * @param radius radius of oval to generate in cell
     * @return random position(up left point of surrounding square) in given cell
     * of oval with given radius so that it does not intersects any walls
     */
    private Point2D.Double randOvalInCell(Cell c, double radius) {
        double rXInCell = randDouble(0, cellWidth - 2 * radius);
        double rYInCell = randDouble(0, cellHeight - 2 * radius);

        return new Point2D.Double((cellWidth + wallWidth) * c.col + rXInCell,
                (cellHeight + wallWidth) * c.row + rYInCell);
    }

    /**
     * Returns random double in given interval [min, max)
     * @param min minimum value of random double, inclusive
     * @param max maximum value of random double, exclusive
     * @return random double in given interval [min, max)
     */
    private double randDouble(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    /**
     * Gets oval, it's radius, ovals collection and their radius and checks collision
     * of given oval with any of ovals from collection
     * @param oval oval to check collision with other ovals from collection for
     * @param r radius of oval to check collision with other ovals from collection for
     * @param ovals collection of ovals to check collision with given oval for
     * @param ovalsR radius of ovals from collection of ovals to check collision with given oval for
     * @return true iff givne oval has collision ith any of ovals from collection
     */
    private synchronized boolean ovalConflicts(Point2D.Double oval, double r, Collection<Point2D.Double> ovals, double ovalsR) {
        Iterator<Point.Double> ovalsIt = ovals.iterator();
        while (ovalsIt.hasNext()) {
            if (ovalsIntersect(oval, r, ovalsIt.next(), ovalsR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * gets two oval and their radiuses and checks their collision
     * @param o1 oval 1 to check collision of
     * @param r1 radius of oval 1 to check collision of
     * @param o2 oval 2 to check collision of
     * @param r2 radius of oval 2 to check collision of
     * @return true if given ovals collide each other
     */
    private boolean ovalsIntersect(Point2D.Double o1, double r1, Point2D.Double o2, double r2) {
        return distance(o1.x + r1, o1.y + r1, o2.x + r2, o2.y + r2) < r1 + r2;
    }

    /**
     * Calculates and returns distance between given pointts
     * @param x1 x coordinate of first point
     * @param y1 y coordinate of first point
     * @param x2 x coordinate of second point
     * @param y2 y coordinate of second point
     * @return distance between given points
     */
    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * checks if given cell is one of corner cell of maze
     * @param c cell to check if its corner
     * @return true iff given cell is one of corner cell
     */
    private boolean isCorner(Cell c) {
        return (c.row == 0 || c.row == numRows - 1) && (c.col == 0 || c.col == numCols - 1);
    }

     /* collision methods */

    private synchronized boolean touches(int row, int col, double x, double y) {
        return true;
    }

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
