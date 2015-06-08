package Game.Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

import Game.Model.Cell;
import Game.Model.PlaneMaze;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by SHAKO on 08-Jun-15.
 */
public class PlaneMazeTest {

    int numRows = 14;
    int numCols = 28;
    PlaneMaze pm;

    @Before
    public void setUp() throws Exception {
        pm = new PlaneMaze(numRows, numCols);
    }

    @Test
    public void testNumRows() throws Exception {
        assert pm.numRows() == numRows;
    }

    @Test
    public void testNumCols() throws Exception {
        assert pm.numCols() == numCols;
    }

    @Test
    public void testMakePerfect() throws Exception {
        pm.makePerfect();
        boolean[][] visiteds = new boolean[numRows][numCols];
        Cell start = new Cell(0, 0);
        // bla bla


    }

    @Test
    public void testMakeThiner() throws Exception {

    }

    @Test //@@ aqac iseve rogorc playeris jsonis testshi
    public void testToJsonBuilder() throws Exception {
        pm.makePerfect();
        System.out.println("Look at Json object and check if has structure u wanted: \n" + pm.toJsonBuilder().build());
    }

    @Test
    public void testIsWall() throws Exception {
        // at start does not have walls

        // iterate all cells with this two for-cycle,
        // all walls actually
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                // get down wall of (i, j) cell
                // mus not be down most cell
                if (i < numRows - 1) {
                    assertFalse(pm.isWall(new Cell(i, j), new Cell(i + 1, j)));
                }

                // get left wall of (i, j) cell
                // must not be left most cell
                if (j < numCols - 1) {
                    assertFalse(pm.isWall(new Cell(i, j), new Cell(i, j + 1)));
                }
            }
        }

        Cell c1;
        Cell c2;

        c1 = new Cell(3, 4);
        c2 = new Cell(3, 5);
        pm.setWall(c1, c2, true);
        assert pm.isWall(c1, c2);
        assert pm.isWall(c2, c1);

        c1 = new Cell(13, 6);
        c2 = new Cell(12, 6);
        pm.setWall(c1, c2, true);
        assert pm.isWall(c1, c2);
        assert pm.isWall(c2, c1);
    }

    @Test
    public void testSetWall() throws Exception {
        Cell c1;
        Cell c2;

        c1 = new Cell(3, 2);
        c2 = new Cell(3, 5);
        // must throw exception because cells are not neighbours
        //pm.setWall(c1, c2, true);
        //pm.setWall(c2, c1, true);
    }

    @Test
    public void testCellInBounds() throws Exception {
        assert pm.cellInBounds(new Cell(0, 0));

        assert pm.cellInBounds(new Cell(numRows / 2, numCols / 2));

        assertFalse(pm.cellInBounds(new Cell(numRows, numCols)));
    }
}