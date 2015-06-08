package Game.Model;

/**
 * Created by SHAKO on 07-Jun-15.
 */
public class Cell {

    public int row;
    public int col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * checks if given cells are adjacent, i.e. have same rows(cols) and difference between
     * cols(rows) is -1 or +1
     * @param c1 cell to compare for adjacency
     * @param c2 cell to compare for adjacency
     * @return true iff given cells are adjacent, i.e. have same rows(cols) and difference between
     * cols(rows) is -1 or +1
     */
    public static boolean neighbours(Cell c1, Cell c2) {
        return (Math.abs(c1.row - c2.row) + Math.abs(c1.col - c2.col)) == 1;
    }

    /**
     * compares passed Cell object with this object, checks if they have same
     * row and col
     * @param c will be compared to this
     * @return true iff given and this object have same row and col
     */
    public boolean equals(Cell c) {
        return row == c.row && col == c.col;
    }
}
