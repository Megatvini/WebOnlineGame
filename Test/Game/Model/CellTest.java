package Game.Model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

import Game.Model.Cell;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by SHAKO on 08-Jun-15.
 */
public class CellTest {

    @Test
    public void testNeighbours() throws Exception {
        // cells with same row and col
        assertFalse(Cell.neighbours(new Cell(-3, 13), new Cell(-3, 13)));
        assertFalse(Cell.neighbours(new Cell(17, 0), new Cell(17, 0)));

        // cells with same row(col) but col(row) difference is not 1 or -1
        assertFalse(Cell.neighbours(new Cell(5, 4), new Cell(5, 6)));
        assertFalse(Cell.neighbours(new Cell(10, 3), new Cell(0, 3)));

        // adjacent cells
        assert(Cell.neighbours(new Cell(4, 13), new Cell(4, 12)));
        assert(Cell.neighbours(new Cell(41, -1), new Cell(40, -1)));

    }

    @Test
    public void testEquals() throws Exception {
        // equals
        assert(new Cell(3, 0).equals(new Cell(3, 0)));

        // different with row xor col
        assertFalse(new Cell(12, -2).equals(new Cell(12, 14)));
        assertFalse(new Cell(7, 4).equals(new Cell(2, 4)));

        // different with row and col
        assertFalse(new Cell(41, 3).equals(new Cell(3, 34)));
    }
}