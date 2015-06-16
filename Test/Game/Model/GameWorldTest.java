package Game.Model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.servlet.jsp.jstl.core.Config;
import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by SHAKO on 15-Jun-15.
 * Fully test game world class.
 */
public class GameWorldTest {

    String fileName = "ConfigFile.properties";

    @Spy Configuration config = new Configuration(fileName);
    @Spy PlaneMaze pm = new PlaneMaze(config.getNumRows(), config.getNumCols());
    ConcurrentMap<String, Player> nameOnPlayer;
    List<Point2D.Double> potions;
    GameWorld gw;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        nameOnPlayer = new ConcurrentHashMap<>();
        potions = new ArrayList<>();
        gw = new GameWorld(pm, config, nameOnPlayer, potions, GameWorld.State.NEW);
    }

    @Test
    public void testAddPlayerAtCornerBasics() throws Exception {
        reset(config);
        reset(pm);

        int testCount = 1;
        for (int i = 0; i < testCount; i++) {
            for (int j = 0; j < config.getMaxPlayers(); j++) {
                gw.addPlayerAtCorner(Integer.toString(j));
            }

            for (int j = 0; j < config.getMaxPlayers(); j++) {
                assert nameOnPlayer.keySet().contains(Integer.toString(j));
            }

            for (Player p : nameOnPlayer.values()) {
                Cell cell = getCell(p.getPosition(), config.getPRadius());
                assert cell.equals(getCornerCell(Integer.parseInt(p.getName())));
                assert circleIsInCell(p.getPosition(), config.getPRadius(), cell);
            }
        }
    }

    @Test
    public void testAddPlayerAtCornerEdgeCases() throws Exception {
        reset(config);
        reset(pm);

        for (int j = 0; j < config.getMaxPlayers(); j++) {
            gw.addPlayerAtCorner(Integer.toString(j));
        }

        for (int j = 0; j < config.getMaxPlayers(); j++) {
            assert !gw.addPlayerAtCorner(Integer.toString(j));
        }

        for (int j = 0; j < config.getMaxPlayers(); j++) {
            assert !gw.addPlayerAtCorner(Integer.toString(j + config.getMaxPlayers()));
        }

    }

    @Test
    public void testAddPlayerAtRandom() throws Exception {
        // todo kind of extension, can be tested later
    }

    @Test
    public void testAddPlayerInCell() throws Exception {
        // todo kind of extension, can be tested later
    }

    @Test
    public void testRemovePlayer() throws Exception {
        // todo kind of extension, can be tested later
    }

    @Test
    public void testAddPotAtRandomWithOutPlayersCornersAllowed() throws Exception {
        reset(config);
        reset(pm);

        doReturn(0).when(config).getStartPotNum();

        int potCount = 1;

        for (int i = 0; i < potCount; i++) {
            gw.addPotAtRandom(true);
        }

        assert potions.size() == potCount;

        for (int i = 0; i < potions.size(); i++) {
            Point2D.Double pot = potions.get(i);
            assert circleIsInCell(pot, config.getPotRadius(), getCell(pot, config.getPotRadius()));
        }

    }

    @Test
    public void testAddPotAtRandomWithOutPlayersCornersNotAllowed() throws Exception {
        reset(config);
        reset(pm);

        when(config.getStartPotNum()).thenReturn(0);

        int potCount = 1;
        for (int i = 0; i < potCount; i++) {
            gw.addPotAtRandom(false);
        }
        int potsAtCorner = 0;
        for (int i = 0; i < potions.size(); i++) {
            Point2D.Double pot = potions.get(i);
            if (isCorner(getCell(pot, config.getPotRadius()))) {
                potsAtCorner++;
            }
        }

        assert potions.size() == potCount;
        assert potsAtCorner == 0;

    }

    @Test
    public void testAddPotInCell() throws Exception {
        // todo kind of extension, can be tested later
    }

    @Test
    public void testRemoveLastPot() throws Exception {
        // todo kind of extension, can be tested later
    }

    @Test
    public void testStartGame() throws Exception {

    }

    @Test
    public void testPlayerMove() throws Exception {

    }

    @Test
    public void testSetPlayerCoordinates() throws Exception {
        /*reset(config);
        reset(pm);

        // so putting player, during game running, on any position wont be a problem
        double w = config.getWidth();
        double h = config.getHeight();
        when(config.getMaxMove()).thenReturn(w + h);
        gw = new GameWorld(pm, config, nameOnPlayer, potions, GameWorld.State.RUNNING);


        String[] playerNames = new String[config.getMaxPlayers()];
        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i] = Integer.toString(i);
        }

        *//*
        There are players in game and we have access on all of them and there are
        potions in game and we also have access on all of them so lets simulate player movements
        and test game behaviour. Also there is no collision to walls.
         *//*

        // test if player taking potion on its move
        nameOnPlayer.clear();
        potions.clear();
        nameOnPlayer.put(playerNames[0], new Player(playerNames[0]));
        potions.add(randOval(config.getPotRadius()));
        gw.setPlayerCoordinates(playerNames[0], potions.get(0).x + config.getPotRadius() - config.getPRadius(),
                potions.get(0).y + config.getPotRadius() - config.getPRadius());
        assert potions.isEmpty();

        // test if player taking all potions on its move
        nameOnPlayer.clear();
        potions.clear();
        nameOnPlayer.put(playerNames[0], new Player(playerNames[0]));
        potions.add(randOval(config.getPotRadius()));
        potions.add(randOval(config.getPotRadius()));
        potions.add(randOval(config.getPotRadius()));
        potions.get(1).setLocation(potions.get(0));
        potions.get(2).setLocation(potions.get(0));
        gw.setPlayerCoordinates(playerNames[0], potions.get(0).x + config.getPotRadius() - config.getPRadius(),
                potions.get(0).y + config.getPotRadius() - config.getPRadius());
        assert potions.isEmpty();

        // test if one with more potions killing player, (makes its active false)
        // if it gets closer then dist variable, to be sure in this test players will
        // not intersect each other only their dist will be smaller then determined.
        // check if winners pot num increased
        nameOnPlayer.clear();
        potions.clear();
        int winnersPotNum = 1;
        int losersPotNum = winnersPotNum - 1;
        Player winner = new Player(playerNames[0]);
        Player loser = new Player(playerNames[1]);
        winner.setPotNum(winnersPotNum);
        loser.setPotNum(losersPotNum);
        nameOnPlayer.put(winner.getName(), winner);
        nameOnPlayer.put(loser.getName(), loser);
        Point2D.Double posToPut = getCircleAtDistance(loser.getPosition(),
                config.getPRadius(),
                config.getPRadius(),
                config.getPRadius() + gw.getMinDist() / 2);
        gw.setPlayerCoordinates(winner.getName(), posToPut.x, posToPut.y);
        assert !loser.getActive();
        System.out.println(winner.getPotNum());
        assert winner.getPotNum() == winnersPotNum + config.getPotForKick();
*/
    }

    @Test
    public void testNumberOfPlayers() throws Exception {

    }

    @Test
    public void testGetPlayerNames() throws Exception {

    }

    @Test
    public void testIsFinished() throws Exception {

    }

    @Test
    public void testGetInit() throws Exception {
        System.out.println(gw.getInit());
    }

    @Test
    public void testGetUpdate() throws Exception {
        Configuration config = Configuration.getInstance();
        PlaneMaze pm = new PlaneMaze(config.getNumRows(), config.getNumCols());
        GameWorld gw = new GameWorld(pm, config);
        gw.addPlayerAtCorner("nika");
        gw.addPlayerAtCorner("rezo");
        gw.startGame();
        System.out.println(gw.getUpdate("nika"));

    }

    @Test
    public void testFinishGame() throws Exception {

    }

    /* helper methods for testing */

    Random rand = new Random();

    private Cell getCell(Point2D.Double pos, double radius) {
        double middleX = pos.x + radius;
        double middleY = pos.y + radius;
        int row = (int)(middleY / (config.getCellHeight() + config.getWallWidth()));
        int col = (int)(middleX / (config.getCellWidth() + config.getWallWidth()));
        if (middleX > (col + 1) * config.getCellWidth() + col * config.getWallWidth() ||
                middleY > (row + 1) * config.getCellHeight() + row * config.getWallWidth()) {
            throw new RuntimeException("circle is on wall!");
        }
        Cell c = new Cell(row, col);
        return c;
    }

    private boolean circleIsInCell(Point2D.Double pos, double radius, Cell c) {
        return pointInRect(pos, c.col * (config.getCellWidth() + config.getWallWidth()),
                c.row * (config.getCellHeight() + config.getWallWidth()),
                config.getCellWidth() - radius,
                config.getCellHeight() - radius);
    }

    private boolean pointInRect(Point2D.Double point, double rX, double rY, double rW, double rH) {
        return point.x > rX && point.x < rX + rW &&
                point.y > rY && point.y < rY + rH;
    }

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
                c = new Cell(0, config.getNumCols() - 1);
                break;
            case 2:
                c = new Cell(config.getNumRows() - 1, config.getNumCols() - 1);
                break;
            case 3:
                c = new Cell(config.getNumRows() - 1, 0);
                break;
            default:
                throw new RuntimeException("Index must be in [0, 3] interval!");
        }
        return c;
    }

    /**
     * checks if given cell is one of corners of game map
     * @param c cell to check if it is corner cell
     * @return true iff given cell is one of corner cells of game map
     */
    private boolean isCorner(Cell c) {
        return (c.row == 0 || c.row == config.getNumRows() - 1) &&
                (c.col == 0 || c.col == config.getNumCols() - 1);
    }

    private Point2D.Double randOval(double radius) {
        int randRow = rand.nextInt(config.getNumRows());
        int randCol = rand.nextInt(config.getNumCols());
        return randOvalInCell(new Cell(randRow, randCol), radius);
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
        double rXInCell = randDouble(0, config.getCellWidth() - 2 * radius);
        double rYInCell = randDouble(0, config.getCellHeight() - 2 * radius);

        return new Point2D.Double((config.getCellWidth() + config.getWallWidth()) * c.col + rXInCell,
                (config.getCellHeight() + config.getWallWidth()) * c.row + rYInCell);
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

    /*
     * gets point representing rect's up-left point surrounding circle with given radius,
     * returns circle with given radius(actually up-left point of rect surrounding it) distanced
     * by given double
     */
    private Point2D.Double getCircleAtDistance(Point2D.Double c, double cRadius, double retCircleRad, double dist) {
        Point2D.Double retCircle = new Point2D.Double();
        retCircle.x = c.x + cRadius + dist - retCircleRad;
        retCircle.y = c.y + cRadius - retCircleRad;
        return retCircle;
    }
}