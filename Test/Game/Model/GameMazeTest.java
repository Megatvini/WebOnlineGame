package Game.Model;

import com.sun.corba.se.impl.oa.poa.POAPolicyMediatorImpl_NR_UDS;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by SHAKO on 30-Jun-15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GameMazeTest {

    //@@ give rectangular shape to maze

    // instance to generate pseudo random things(int, double, boolean ...)
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

    @Spy
    Configuration config = Configuration.getInstance();
    GameMaze gm;

    @Before
    public void setUp() throws Exception {
        makeMazeSquare();
        gm = new GameMaze(config);
        readConfig(config);
    }

    @Test
    public void testAddPlayerAtCornerBasics() throws Exception {
        reset(config);

        String playerName = "shako";
        String player1Name = "nika";
        String player2Name = "rezo";
        String player3Name = "guka";

        assert gm.addPlayerAtCorner(playerName);
        Point2D.Double playerPos = gm.getPlPosition(playerName);
        assert getCornerCell(0).equals(getCell(playerPos.x, playerPos.y, pRadius));
        assert !gm.collideWall(pRadius, playerPos.x, playerPos.y);

        assert gm.addPlayerAtCorner(player1Name);
        Point2D.Double player1Pos = gm.getPlPosition(player1Name);
        assert getCornerCell(1).equals(getCell(player1Pos.x, player1Pos.y, pRadius));
        assert !gm.collideWall(pRadius, player1Pos.x, player1Pos.y);


        assert gm.addPlayerAtCorner(player2Name);
        Point2D.Double player2Pos = gm.getPlPosition(player2Name);
        assert getCornerCell(2).equals(getCell(player2Pos.x, player2Pos.y, pRadius));
        assert !gm.collideWall(pRadius, player2Pos.x, player2Pos.y);

        assert gm.addPlayerAtCorner(player3Name);
        Point2D.Double player3Pos = gm.getPlPosition(player3Name);
        assert getCornerCell(3).equals(getCell(player3Pos.x, player3Pos.y, pRadius));
        assert !gm.collideWall(pRadius, player3Pos.x, player3Pos.y);

        assert !gm.addPlayerAtCorner(playerName);
        assert !gm.addPlayerAtCorner(player1Name);
        assert !gm.addPlayerAtCorner(player2Name);
        assert !gm.addPlayerAtCorner(player3Name);

        assert !gm.addPlayerAtCorner("extra player");
    }

    @Test
    public void testAddPlayerAtCornerHasCollisionsWithPlayers() throws Exception {
        reset(config);

        Map<String, Point2D.Double> nameOnPos = Collections.synchronizedMap(new HashMap<>());
        GameMaze gm = new GameMaze(config, nameOnPos, Collections.synchronizedSet(new HashSet<>()));

        for (int i = 0; i < 4; i++) {
            nameOnPos.put(Integer.toString(i), randOvalInCell(getCornerCell(i), pRadius));
        }

        when(config.getStartDist()).thenReturn(cellHeight + cellHeight);
        assert !gm.addPlayerAtCorner("colliding player");
    }

    @Test
    public void testAddPlayerAtCornerHasCollisionsWithPotions() throws Exception {
        reset(config);

        double potionRadiusMock = Math.min(cellWidth, cellHeight) / 2;

        when(config.getPotRadius()).thenReturn(potionRadiusMock);
        Set<Point2D.Double> potions = Collections.synchronizedSet(new HashSet<>());
        GameMaze gm = new GameMaze(config, Collections.synchronizedMap(new HashMap<>()), potions);

        for (int j = 0; j < 4; j++) {
            potions.add(randOvalInCell(getCornerCell(j), potionRadiusMock));
        }

        assert !gm.addPlayerAtCorner("colliding player");
    }

    @Test
    public void testAddPlayerAtRandomBasics() throws Exception {
        reset(config);

        int playerNum = 1000;
        for (int i = 0; i < playerNum; i++) {
            String name = "shako";
            if (gm.addPlayerAtRandom(name)) {
                Point2D.Double plPos = gm.getPlPosition(name);
                assert gm.cellInBounds(getCell(plPos.x, plPos.y, pRadius));
                assert !gm.collideWall(pRadius, plPos.x, plPos.y);
            }
        }
    }

    @Test
    public void testAddPlayerAtRandomHasCollisionWithPlayers() throws Exception {
        reset(config);

        when(config.getStartDist()).thenReturn(cellHeight + cellHeight);
        Map<String, Point2D.Double> nameOnPos = Collections.synchronizedMap(new HashMap<>());
        GameMaze gm = new GameMaze(config, nameOnPos, Collections.synchronizedSet(new HashSet<>()));

        for (int i = 0; i < gm.numRows; i++) {
            for (int j = 0; j < gm.numCols; j++) {
                Point2D.Double pos = randOvalInCell(new Cell(i, j), pRadius);
                nameOnPos.put(i + ":" + j, pos);
            }
        }

        int tryNum = 5;
        for (int i = 0; i < tryNum; i++) {
            assert !gm.addPlayerAtRandom("colliding player");
        }

    }

    @Test
    public void testAddPlayerAtRandomHasCollisionWithPotions() throws Exception {
        reset(config);

        double potionRadiusMock = Math.min(cellWidth, cellHeight) / 2;
        when(config.getPotRadius()).thenReturn(potionRadiusMock);
        Set<Point2D.Double> potions = Collections.synchronizedSet(new HashSet<>());
        GameMaze gm = new GameMaze(config, Collections.synchronizedMap(new HashMap<>()), potions);

        for (int i = 0; i < gm.numRows; i++) {
            for (int j = 0; j < gm.numCols; j++) {
                potions.add(randOvalInCell(new Cell(i, j), potionRadiusMock));
            }
        }

        int tryNum = 5;
        for (int i = 0; i < tryNum; i++) {
            assert !gm.addPlayerAtRandom("colliding player");
        }
    }


    @Test
    public void testAddPlayerInCellBasics() throws Exception {
        reset(config);

        when(config.getStartDist()).thenReturn(0.0);
        GameMaze gm = new GameMaze(config);

        for (int i = 0; i < gm.numRows; i++) {
            for (int j = 0; j < gm.numCols; j++) {
                String name = i + ":" + j;
                Cell c = new Cell(i, j);
                assert gm.addPlayerInCell(name, c);
                Point2D.Double pos = gm.getPlPosition(name);
                assert !gm.collideWall(pRadius, pos.x, pos.y);
            }
        }
    }

    @Test
    public void testAddPlayerInCellHasCollisionWithPlayers() throws Exception {
        reset(config);

        when(config.getStartDist()).thenReturn(cellHeight + cellHeight);
        Map<String, Point2D.Double> nameOnPos = Collections.synchronizedMap(new HashMap<>());
        GameMaze gm = new GameMaze(config, nameOnPos, Collections.synchronizedSet(new HashSet<>()));

        for (int i = 0; i < gm.numRows; i++) {
            for (int j = 0; j < gm.numCols; j++) {
                Point2D.Double pos = randOvalInCell(new Cell(i, j), pRadius);
                nameOnPos.put(i + ":" + j, pos);
            }
        }

        for (int i = 0; i < gm.numRows; i++) {
            for (int j = 0; j < gm.numCols; j++) {
                Point2D.Double pos = randOvalInCell(new Cell(i, j), pRadius);
                assert !gm.addPlayerInCell(i + ":" + j, new Cell(i, j));
            }
        }

    }

    @Test
    public void testAddPotAtRandomBasicsConflictAllowed() throws Exception {
        reset(config);

        int testNum = 1000;
        for (int i = 0; i < testNum; i++) {
            Point2D.Double pot = gm.addPotAtRandom(true);
            if (pot == null) continue;
            assert !gm.collideWall(potRadius, pot.x, pot.y);
        }

    }

    @Test
    public void testAddPotAtRandomConflictNotAllowedHasCollisionWithPlayers() throws Exception {
        reset(config);

        double potionAndPlayerRadiusMock = Math.min(cellWidth, cellHeight) / 2;
        when(config.getPotRadius()).thenReturn(potionAndPlayerRadiusMock);
        when(config.getPRadius()).thenReturn(potionAndPlayerRadiusMock);
        Map<String, Point2D.Double> nameOnPos = Collections.synchronizedMap(new HashMap<>());
        GameMaze gm = new GameMaze(config, nameOnPos, Collections.synchronizedSet(new HashSet<>()));

        for (int i = 0; i < gm.numRows; i++) {
            for (int j = 0; j < gm.numCols; j++) {
                Point2D.Double pos = randOvalInCell(new Cell(i, j), potionAndPlayerRadiusMock);
                nameOnPos.put(i + ":" + j, pos);
            }
        }

        int tryNum = 5;
        for (int i = 0; i < tryNum; i++) {
            assert gm.addPotAtRandom(false) == null;
        }
    }

    @Test
    public void testAddPotInCellBasicsConflictAllowed() throws Exception {
        reset(config);

        for (int i = 0; i < gm.numRows; i++) {
            for (int j = 0; j < gm.numCols; j++) {
                Cell c = new Cell(i, j);
                Point2D.Double pot = gm.addPotInCell(c, true);
                if (pot == null) continue;
                assert getCell(pot.x, pot.y, potRadius).equals(c);
            }
        }
    }

    @Test
    public void testAddPotInCellConflictNotAllowedHasCollisionWithPlayers() throws Exception {
        reset(config);

        double potionAndPlayerRadiusMock = Math.min(cellWidth, cellHeight) / 2;
        when(config.getPotRadius()).thenReturn(potionAndPlayerRadiusMock);
        when(config.getPRadius()).thenReturn(potionAndPlayerRadiusMock);
        Map<String, Point2D.Double> nameOnPos = Collections.synchronizedMap(new HashMap<>());
        GameMaze gm = new GameMaze(config, nameOnPos, Collections.synchronizedSet(new HashSet<>()));

        for (int i = 0; i < gm.numRows; i++) {
            for (int j = 0; j < gm.numCols; j++) {
                Point2D.Double pos = randOvalInCell(new Cell(i, j), potionAndPlayerRadiusMock);
                nameOnPos.put(i + ":" + j, pos);
            }
        }

        int tryNum = 5;
        for (int i = 0; i < tryNum; i++) {
            Cell randomCell = new Cell(rand.nextInt(gm.numRows - 1), rand.nextInt(gm.numCols - 1));
            assert gm.addPotInCell(randomCell, false) == null;
        }
    }


    @Test
    public void testRemovePlayer() throws Exception {
        String name = "shako";
        gm.addPlayerAtRandom(name);
        assert gm.getPlPosition(name).equals(gm.removePlayer(name));
        assert gm.removePlayer(name) == null;
    }

    @Test
    public void testRemovePot() throws Exception {
        int potNumToAdd = 1000;
        List<Point2D.Double> addedPots = new ArrayList<>();
        for (int i = 0; i < potNumToAdd; i++) {
            addedPots.add((Point2D.Double) gm.addPotAtRandom(true).clone());

        }

        Set<Point2D.Double> addPotsInGM = gm.getPotions();
        for (int i = 0; i < addedPots.size(); i++) {
            gm.removePot(addedPots.get(i));
        }

        assert gm.getPotions().size() == 0;
    }

    @Test
    public void testLongMove() throws Exception {
        String name = "shako";
        gm.addPlayerAtRandom(name);
        Point2D.Double pos = gm.getPlPosition(name);
        double dist = gm.getDist();
        assert gm.longMove(name, pos.x + dist, pos.y + dist);
    }

    @Test
    public void testCollideWall() throws Exception {
        // todo white this test method
    }

    @Test
    public void testGetDist() throws Exception {
        assert config.getStartDist() == gm.getDist();
    }

    @Test
    public void testIncreaseDist() throws Exception {
        double plusDist = 55.4;
        double oldDist = gm.getDist();
        gm.increaseDist(plusDist);
        assert oldDist + plusDist == gm.getDist();
    }

    @Test
    public void testPlusPlayerPos() throws Exception {
        String name = "shako";
        gm.addPlayerAtRandom(name);
        Point2D.Double oldPos = gm.getPlPosition(name);
        double plusX = 45.23;
        double plusY = 12.45;
        gm.plusPlayerPos(name, plusX, plusY);
        Point2D.Double newPos = gm.getPlPosition(name);
        assert oldPos.x + plusX == newPos.x
                && oldPos.y + plusY == newPos.y;
    }

    @Test
    public void testCollidedPotions() throws Exception {
        reset(config);

        double potionAndPlayerRadiusMock = Math.min(cellWidth, cellHeight) / 2;
        when(config.getPotRadius()).thenReturn(potionAndPlayerRadiusMock);
        Set<Point2D.Double> potions = Collections.synchronizedSet(new HashSet<>());
        Map<String, Point2D.Double> nameOnPos = Collections.synchronizedMap(new HashMap<>());
        GameMaze gm = new GameMaze(config, nameOnPos, potions);

        Cell c = new Cell(0, 0);

        String name = "shako";
        Point2D.Double playerPos = randOvalInCell(c, potionAndPlayerRadiusMock);
        nameOnPos.put(name, playerPos);

        int potNumInCell = 10;
        for (int i = 0; i < potNumInCell; i++) {
            Point2D.Double pot = randOvalInCell(c, potionAndPlayerRadiusMock);
            potions.add(pot);
        }

        Iterator<Point2D.Double> collidedPotsIt = gm.collidedPotions(name).iterator();
        while (collidedPotsIt.hasNext()) {
            Point2D.Double nextPot = collidedPotsIt.next();
            assert potions.contains(nextPot);
        }
    }

    @Test
    public void testCollidedPlayersToPlayer() throws Exception {
        reset(config);

        double distMock = cellWidth + cellHeight;
        when(config.getPRadius()).thenReturn(distMock);
        Map<String, Point2D.Double> nameOnPos = Collections.synchronizedMap(new HashMap<>());
        GameMaze gm = new GameMaze(config, nameOnPos, Collections.synchronizedSet(new HashSet<>()));

        Cell c = new Cell(0, 0);

        String name = "shako";
        Point2D.Double playerPos = randOvalInCell(c, pRadius);
        nameOnPos.put(name, playerPos);

        int plNumInCell = 10;
        for (int i = 0; i < plNumInCell; i++) {
            Point2D.Double collidingPlayerPos = randOvalInCell(c, pRadius);
            nameOnPos.put(Integer.toString(i), collidingPlayerPos);
        }

        Iterator<String> collidedPlayersNamesIt = gm.collidedPlayers(name).iterator();
        while (collidedPlayersNamesIt.hasNext()) {
            String nextName = collidedPlayersNamesIt.next();
            assert nameOnPos.containsKey(nextName);
        }
    }

    @Test
    public void testCollidedPlayersToPotion() throws Exception {
        reset(config);

        double potionAndPlayerRadiusMock = Math.min(cellWidth, cellHeight) / 2;
        when(config.getPRadius()).thenReturn(potionAndPlayerRadiusMock);
        Set<Point2D.Double> potions = Collections.synchronizedSet(new HashSet<>());
        Map<String, Point2D.Double> nameOnPos = Collections.synchronizedMap(new HashMap<>());
        GameMaze gm = new GameMaze(config, nameOnPos, potions);

        Cell c = new Cell(0, 0);

        Point2D.Double potion = randOvalInCell(c, potRadius);
        potions.add(potion);

        int plNumInCell = 10;
        for (int i = 0; i < plNumInCell; i++) {
            Point2D.Double collidingPlayerPos = randOvalInCell(c, pRadius);
            nameOnPos.put(Integer.toString(i), collidingPlayerPos);
        }

        Iterator<String> collidedPlayersNamesIt = gm.collidedPlayers(potion).iterator();
        while (collidedPlayersNamesIt.hasNext()) {
            String nextName = collidedPlayersNamesIt.next();
            assert nameOnPos.containsKey(nextName);
        }
    }

    @Test
    public void testConfigJsonBuilder() throws Exception {
        // look at it and check lol
        System.out.println(gm.configJsonBuilder());
    }

    /*
    helper methods for GameMaze testing class
     */

    private void makeMazeSquare() {
        double width = 500;
        double height = 500;
        int numRows = 10;
        int numCols = 10;
        double wallWidth = 4;
        double pRadius = 8;
        double potRadius = 5;
        double maxMove = 100;

        when(config.getWidth()).thenReturn(width);
        when(config.getHeight()).thenReturn(height);
        when(config.getNumRows()).thenReturn(numRows);
        when(config.getNumCols()).thenReturn(numCols);
        when(config.getWallWidth()).thenReturn(wallWidth);
        when(config.getPRadius()).thenReturn(pRadius);
        when(config.getPotRadius()).thenReturn(potRadius);
        when(config.getMaxMove()).thenReturn(maxMove);

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

    private Cell getCell(double x, double y, double radius) {
        double cX = x + radius;
        double cY = y + radius;
        int rowIndx = (int)(cY / (cellHeight + wallWidth));
        int colIndx = (int)(cX / (cellWidth + wallWidth));
        if (cY > rowIndx * (cellHeight + wallWidth) + cellHeight) return null;
        if (cX > colIndx * (cellWidth + wallWidth) + cellWidth) return null;
        return new Cell(rowIndx, colIndx);
    }

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
                c = new Cell(0, gm.numCols - 1);
                break;
            case 2:
                c = new Cell(gm.numRows - 1, gm.numCols - 1);
                break;
            case 3:
                c = new Cell(gm.numRows - 1, 0);
                break;
            default:
                throw new IllegalArgumentException("Index must be in [0, 3] interval!");
        }
        return c;
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

    public double minDouble(double a, double b) {
        if (a < b) return a;
        return b;
    }

}