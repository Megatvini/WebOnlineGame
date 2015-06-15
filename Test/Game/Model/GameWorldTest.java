package Game.Model;

import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by SHAKO on 15-Jun-15.
 * Fully test game world class.
 */
public class GameWorldTest {

    PlaneMaze pm;

    GameWorld gw;

    @Before
    public void setUp() throws Exception {
        pm = new PlaneMaze(GameWorld.numRows, GameWorld.numCols);
        gw = new GameWorld(pm);
    }

    @Test
    public void testAddPlayerAtCornerBasics() throws Exception {
        int testCount = 100;
        for (int i = 0; i < testCount; i++) {
            for (int j = 0; j < GameWorld.maxPlayers; j++) {
                gw.addPlayerAtCorner(Integer.toString(j));
            }

            Collection<String> playerNames = gw.getPlayers();

            for (int j = 0; j < GameWorld.maxPlayers; j++) {
                assert playerNames.contains(Integer.toString(j));
            }

            for (int j = 0; j < GameWorld.maxPlayers; j++) {
                Player player = gw.getPlayer(Integer.toString(j));
                Cell cell = getCell(player.getPosition(), GameWorld.pRadius);
                assert cell.equals(getCornerCell(j));
                assert circleIsInCell(player.getPosition(), GameWorld.pRadius, cell);
            }
        }
    }

    @Test
    public void testAddPlayerAtCornerEdgeCases() throws Exception {
        for (int j = 0; j < GameWorld.maxPlayers; j++) {
            gw.addPlayerAtCorner(Integer.toString(j));
        }

        for (int j = 0; j < GameWorld.maxPlayers; j++) {
            assert !gw.addPlayerAtCorner(Integer.toString(j));
        }

        for (int j = 0; j < GameWorld.maxPlayers; j++) {
            assert !gw.addPlayerAtCorner(Integer.toString(j + GameWorld.maxPlayers));
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
        int potCount = 1000;

        for (int i = 0; i < potCount; i++) {
            gw.addPotAtRandom(true);
        }

        List<Point2D.Double> potions = gw.getPotions();

        assert potions.size() - GameWorld.startPotNum == potCount;

        for (int i = 0; i < potions.size(); i++) {
            Point2D.Double pot = potions.get(i);
            assert circleIsInCell(pot, GameWorld.potRadius, getCell(pot, GameWorld.potRadius));
        }

    }

    @Test
    public void testAddPotAtRandomWithOutPlayersCornersNotAllowed() throws Exception {
        int potsAtCorner = 0;
        List<Point2D.Double> potions = gw.getPotions();
        for (int i = 0; i < potions.size(); i++) {
            Point2D.Double pot = potions.get(i);
            if (isCorner(getCell(pot, GameWorld.potRadius))) {
                potsAtCorner++;
            }
        }

        int potCount = 10000;
        for (int i = 0; i < potCount; i++) {
            gw.addPotAtRandom(false);
        }
        int potsAtCornerAfter = 0;
        potions = gw.getPotions();
        for (int i = 0; i < potions.size(); i++) {
            Point2D.Double pot = potions.get(i);
            if (isCorner(getCell(pot, GameWorld.potRadius))) {
                potsAtCornerAfter++;
            }
        }

        assert potsAtCorner == potsAtCornerAfter;
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

    }

    @Test
    public void testGetUpdate() throws Exception {

    }

    @Test
    public void testFinishGame() throws Exception {

    }

    /* helper methods for testing */

    private Cell getCell(Point2D.Double pos, double radius) {
        double middleX = pos.x + radius;
        double middleY = pos.y + radius;
        int row = (int)(middleY / (GameWorld.cellHeight + GameWorld.wallWidth));
        int col = (int)(middleX / (GameWorld.cellWidth + GameWorld.wallWidth));
        if (middleX > (col + 1) * GameWorld.cellWidth + col * GameWorld.wallWidth ||
                middleY > (row + 1) * GameWorld.cellHeight + row * GameWorld.wallWidth) {
            throw new RuntimeException("circle is on wall!");
        }
        Cell c = new Cell(row, col);
        return c;
    }

    private boolean circleIsInCell(Point2D.Double pos, double radius, Cell c) {
        return pointInRect(pos, c.col * (GameWorld.cellWidth + GameWorld.wallWidth),
                c.row * (GameWorld.cellHeight + GameWorld.wallWidth),
                GameWorld.cellWidth - radius,
                GameWorld.cellHeight - radius);
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
     * checks if given cell is one of corners of game map
     * @param c cell to check if it is corner cell
     * @return true iff given cell is one of corner cells of game map
     */
    private boolean isCorner(Cell c) {
        return (c.row == 0 || c.row == GameWorld.numRows - 1) &&
                (c.col == 0 || c.col == GameWorld.numCols - 1);
    }

}