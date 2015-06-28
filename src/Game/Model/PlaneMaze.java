package Game.Model;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import java.util.Random;

/**
 * Class represents maze, without visualization, just information were are and
 * where are not walls, main use is to make random perfect maze, which is maze where there is a
 * way from each cell to each cell, and this there is not two different ways between any two
 * cell, different way are ways which are not absolutely same. Maze will be wih out border walls,
 * its up to user, he can consider that maze have all border walls or does not have any.
 */

public class PlaneMaze {

    // number of walls of cell
    private static final int cellWallNum = 4;

    // instance to generate pseudo random things(int, double, boolean ...)
    private  Random rand = new Random();

    // main structure of maze
    private CellInner[][] cells;

    // number of rows in maze
    protected int numRows;

    // number of columns in maze
    protected int numCols;

    /**
     * creates maze with out any walls. with given number of rows and columns.
     * maze does not have border walls also.
     * @param numRows number of rows in maze
     * @param numCols number of columns in maze
     * @throws RuntimeException if number of rows or columns are lower then zero
     */
    public PlaneMaze(int numRows, int numCols) {

        try {
            if (numRows < 0 || numCols < 0) {
                throw new RuntimeException("Number of rows and number of columns must be equal or greater than zero!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.numRows = numRows;
        this.numCols = numCols;

        cells = new CellInner[this.numRows][this.numCols];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new CellInner(cellWallNum);
            }
        }
    }

    /**
     * returns number of rows of this maze
     * @return number of rows of this maze
     */
    public int numRows() {
        return numRows;
    }


    /**
     * returns number of columns of this maze
     * @return number of columns of this maze
     */
    public int numCols() {
        return numCols;
    }

    /**
     * makes any representation of maze perfect, which is maze where there is a
     * way from each cell to each cell, and this there is not two different ways between any two
     * cell, different way are ways which are not absolutely same.
     * @return this object
     */
    public PlaneMaze  makePerfect() {
        makeEmpty();
        int[] conn = new int[numCols];
        for (int i = 0; i < numCols; i++) {
            conn[i] = i;
        }
        for (int i = 0; i < numRows ; i++) {
            if (i > 0) {
                for (int j = 0; j < numCols; j++) {
                    if (isWall(new Cell(i, j), new Cell(i - 1, j))) {
                        conn[j] = i * numCols + j;
                    }
                }
            }
            createHorWalls(conn, i);
            createVerWalls(conn, i);
        }
        return this;
    }

    /**
     * iterates all walls and with given probability decides delete or not.
     * if probability is zero, deletes nothing, if probability is 1 deletes every walls.
     * @param prob probability of deleting wall
     */
    public void makeThinner(double prob) {
        if (prob < 0 || prob > 1) {
            try {
                throw new Exception("probability must be in range [0, 1]");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Cell curr = new Cell(i, j);
                if (i < numRows - 1) {
                    Cell down = new Cell(i + 1, j);
                    if (rand.nextDouble() < prob) {
                        setWall(curr, down, false);
                    }
                }
                if (j < numCols - 1) {
                    Cell right = new Cell(i, j + 1);
                    if (rand.nextDouble() < prob) {
                        setWall(curr, right, false);
                    }
                }
            }
        }
    }

    /**
     * Checks if given cell is in bounds, which means cell's row is not less
     * then zero and is less than number of rows, cell's col is not
     * less then zero and is less than number of columns
     * @param c cell to check if it is in bounds
     * @return true iff given cell is in bounds
     */
    public boolean cellInBounds(Cell c) {
        return cellInBounds(c.row, c.col);
    }

    /**
     * Checks if given cell with given row and column is in bounds, which
     * means row is not less then zero and is less than number of rows, col is not
     * less then zero and is less than number of columns
     * @param row row index to check
     * @param col column index to check
     * @return true iff cell with given row and column is in bounds
     */
    public boolean cellInBounds(int row, int col) {
        return (row >= 0 && row < numRows && col >=0 && col < numCols);
    }

    /**
     * Check if there is wall in this maze between neighbour given two
     * cells with given rows and columns.
     * @param r1 row index of first cell
     * @param c1 column index of first cell
     * @param r2 row index of second cell
     * @param c2 column index of second cell
     * @return true iff there is wall between given cells
     * @throws IllegalArgumentException if given cells are not neighbours
     */
    public boolean isWall(int r1, int c1, int r2, int c2) {
        return isWall(new Cell(r1, c1), new Cell(r2, c2));
    }

    /**
     * Checks if there is wall in this maze for given neighbour two cells.
     * @param p1 first cell
     * @param p2 second cell
     * @return true iff there is wall between given cells
     * @throws IllegalArgumentException if given cells are not neighbours
     */
    public boolean isWall(Cell p1, Cell p2) {
        if (!cellInBounds(p1) || !cellInBounds(p2)) {
            try {
                throw new IllegalArgumentException("Cell out of bounds!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cells[p1.row][p1.col].getWall(neighborDir(p1, p2));
    }


    /**
     * Puts or removes wall between given neighbour cells, depended on givevn
     * boolean.
     * @param p1 first cell
     * @param p2 second cell
     * @param state boolean to decide put or remove wall between given neighbour cells
     * @throws IllegalArgumentException if given cells are not neighbours
     */
    public void setWall(Cell p1, Cell p2, boolean state) {
        if (!cellInBounds(p1) || !cellInBounds(p2)) {
            try {
                throw new Exception("Cell out of bounds!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cells[p1.row][p1.col].setWall(neighborDir(p1, p2), state);
        cells[p2.row][p2.col].setWall(neighborDir(p2, p1), state);
    }

    /**
     * Builds and returns java script object notation object builder representing this object.
     * With specified format like this example:
     * {"numRows":14, "numCols":24, "walls":[{"cell1":{"row":5, "col":8}, "cell2":{"row":5, "col":7}}, {next wall}, ...]}
     * @return java script object notation object builder representing this object.
     */
    public JsonObjectBuilder toJsonBuilder() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonObjectBuilder mazeJson = factory.createObjectBuilder();

        mazeJson.add("numRows", numRows)
                .add("numCols", numCols);

        JsonArrayBuilder wallsJson = factory.createArrayBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Cell curr = new Cell(i, j);
                if (i < numRows - 1) {
                    Cell down = new Cell(i + 1, j);
                    if (isWall(curr, down)) {
                        JsonObjectBuilder wallJson = createWallJson(curr, down);
                        wallsJson.add(wallJson);
                    }
                }
                if (j < numCols - 1) {
                    Cell right = new Cell(i, j + 1);
                    if (isWall(curr, right)) {
                        JsonObjectBuilder wallJson = createWallJson(curr, right);
                        wallsJson.add(wallJson);
                    }
                }
            }
        }

        mazeJson.add("walls", wallsJson);
        return mazeJson;
    }

    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> private methods >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

    /**
     * deletes all walls in this maze object
     */
    private void makeEmpty() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Cell curr = new Cell(i, j);
                if (i < numRows - 1) {
                    Cell down = new Cell(i + 1, j);
                    setWall(curr, down, false);
                }
                if (j < numCols - 1) {
                    Cell right = new Cell(i, j + 1);
                    setWall(curr, right, false);
                }
            }
        }
    }

    /**
     * Puts horizontal walls in given row of maze, with some probability,
     * if neighbour two cells for specified wall already has some path then puts
     * definitely puts wall. Conn object tells weather there is way between two cells
     * in given row, if has same number in conn array then there is way.
     * @param conn tells if there is way between two cells of given row, if has same number in conn array
     *             then there is way.
     * @param rowIndex row index to put horizontal walls in
     */
    private void createHorWalls(int[] conn, int rowIndex) {
        for (int j = 0; j < numCols - 1; j++) {

            boolean addWall;
            if (rowIndex != numRows - 1) addWall = rand.nextDouble() >= 0.5;
            else addWall = false;

            Cell curr = new Cell(rowIndex, j);

            if (conn[j] == conn[j + 1] || addWall) {
                setWall(curr, new Cell(rowIndex, j + 1), true);
            } else {
                int next = conn[j + 1];
                for (int k = 0; k < numCols; k++) {
                    if (conn[k] == next) {
                        conn[k] = conn[j];
                    }
                }
            }
        }
    }

    /**
     * Puts vertical walls under given row cells, with some probability. Avoids isolation.
     * @param conn array telling weather there is wall between given cells, if has same number in conn array
     *             then there is way.
     * @param rowIndex row index to vertical walls under cells
     */
    private void createVerWalls(int[] conn, int rowIndex) {
        int bottom = 1;
        for (int j = 0; j < numCols; j++) {
            boolean addWall = rand.nextDouble() >= 0.3;
            Cell curr = new Cell(rowIndex, j);

            if (rowIndex < numRows - 1) {
                if (j < numCols - 1) {
                    if (conn[j] == conn[j + 1]) {
                        bottom++;
                        if (bottom > 1 && addWall) {
                            setWall(curr, new Cell(rowIndex + 1, j), true);
                            bottom--;
                        }
                    } else {
                        if (bottom > 1 && addWall) {
                            setWall(curr, new Cell(rowIndex + 1, j), true);
                            bottom--;
                        }
                        bottom = 1;
                    }
                } else {
                    if (bottom > 1 && addWall) {
                        setWall(curr, new Cell(rowIndex + 1, j), true);
                        bottom--;
                    }
                }
            }
        }
    }

    /**
     * Builds java script object notation object builder to represent wall. Wall identified
     * with given two neighbour cells. With specified format like this example:
     * {"cell1":{"row":5, "col":8}, "cell2":{"row":5, "col":7}}
     * @param c1 first cell
     * @param c2 second cell
     * @return java script object notation object builder to represent wall between given cells
     * @throws IllegalArgumentException if given cells are not neighbours
     */
    private JsonObjectBuilder createWallJson(Cell c1, Cell c2) {
        if (!Cell.neighbours(c1, c2)) {
            try {
                throw new IllegalArgumentException("Cells are not neighbors!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JsonObjectBuilder wallJson = Json.createObjectBuilder()
                .add("cell1", Json.createObjectBuilder()
                        .add("row", c1.row)
                        .add("col", c1.col))
                .add("cell2", Json.createObjectBuilder()
                        .add("row", c2.row)
                        .add("col", c2.col));
        return wallJson;
    }

    /**
     * For given neighbour cells returns direction from first to another: North, South, East or West.
     * @param c1 first cell
     * @param c2 second cell
     * @return direction from first to another
     * @throws IllegalArgumentException if given cells are not neighbours
     */
    private dirEnum neighborDir(Cell c1, Cell c2) {
        if (!Cell.neighbours(c1, c2)) {
            try {
                throw new IllegalArgumentException("Cells are not neighbors!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (c1.row != c2.row) {
            return (c1.row < c2.row ? dirEnum.North : dirEnum.South);
        } else {
            return (c1.col < c2.col ? dirEnum.East : dirEnum.West);
        }
    }


    // enum from four directions, to use for orientation from one neighbour cell to another
    private enum dirEnum{North, East, South, West}

    /*
    inner class representing cell of maze
     */
    private class CellInner {
        // walls around this cell
        private boolean[] walls;

        /**
         * Constructor for this inner class
         * @param cellWallNum wall number around cell
         */
        private CellInner(int cellWallNum) {
            walls = new boolean[cellWallNum];
        }

        /**
         * Puts or removes one of wall around this cell.
         * @param d direction in which to add or remove wall
         * @param state decides add or remove wall
         */
        private void setWall(dirEnum d, boolean state) {
            walls[d.ordinal()] = state;
        }

        /**
         * Gets if there is wall at given direction in this cell
         * @param d direction in which to check wall
         * @return true iff there is wall in this cell for specified direction
         */
        private boolean getWall(dirEnum d) {
            return walls[d.ordinal()];
        }
    }

}






























