package Game.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * class explanation @@
 */

public class PlaneMaze {
    private static int cellWallNum = 4;

    private Random rand = new Random();

    private boolean[][] horizWalls;
    private  boolean[][] vertWalls;

    private int numRows;
    private int numCols;

    /**
     * The constructor initializes a new maze of the specified dimensions.
     * If the hasWalls argument is true, the maze is initially configured with
     * all walls intact. If false, the maze starts with no walls at all.
     */
    public PlaneMaze(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;

        horizWalls = new boolean[numRows - 1][numCols];
        vertWalls = new boolean[numRows][numCols - 1];
    }

    public void  makePerfect() {
        int[] conn = new int[numCols];
        for (int i = 0; i < numCols; i++) {
            conn[i] = i;
        }

        for (int i = 0; i < numRows ; i++) {

            if (i > 0) {
                for (int j = 0; j < numCols; j++) {
                    if (isW(new Cell(i, j), new Cell(i - 1, j))) {
                        conn[j] = i * numCols + j;
                    }
                }
            }

            for (int j = 0; j < numCols; j++) {

                Cell curr = new Cell(i, j);

                if (i != numRows - 1) {
                    if (j < numCols - 1) {
                        if (conn[j] == conn[j + 1] || rand.nextDouble() >= 0.5) {
                            setWall(curr, new Cell(i, j + 1), true);
                        } else {
                            int next = conn[j + 1];
                            for (int k = 0; k < numCols; k++) {
                                if (conn[k] == next) {
                                    conn[k] = conn[j];
                                }
                            }
                        }
                    }
                } else if (j < numCols - 1) {
                    if (conn[j] == conn[j + 1]) {
                        setWall(curr, new Cell(i, j + 1), true);
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

            int bottom = 1;
            for (int j = 0; j < numCols; j++) {
                Cell curr = new Cell(i, j);

                if (i < numRows - 1) {
                    if (j < numCols - 1) {
                        if (conn[j] == conn[j + 1]) {
                            bottom++;
                            if (bottom > 1 && rand.nextDouble() >= 0.3) {
                                setWall(curr, new Cell(i + 1, j), true);
                                bottom--;
                            }
                        } else {
                            if (bottom > 1 && rand.nextDouble() >= 0.3) {
                                setWall(curr, new Cell(i + 1, j), true);
                                bottom--;
                            }
                            bottom = 1;
                        }
                    } else {
                        if (bottom > 1 && rand.nextDouble() >= 0.3) {
                            setWall(curr, new Cell(i + 1, j), true);
                            bottom--;
                        }
                    }
                }

            }


        }


    }

    /**
     *
     * @param prob
     */
    public void makeThiner(double prob) {
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
                if (rand.nextDouble() < prob) {
                    Cell curr = new Cell(i, j);
                    Cell neighbour = null;
                    for (int k = 0; k < cellWallNum; k++) {
                        switch (k) {
                            case 0:
                                neighbour = new Cell(i - 1, j);
                                break;
                            case 1:
                                neighbour = new Cell(i, j + 1);
                                break;
                            case 2:
                                neighbour = new Cell(i + 1, j);
                                break;
                            case 3:
                                neighbour = new Cell(i, j - 1);
                                break;
                            default:
                                break;
                        }
                        if (pointInBounds(neighbour) && isW(curr, neighbour)) {
                            setWall(curr, neighbour, false);
                            break;
                        }
                    }
                }
            }
        }

    }

    /**
     * returns number of rows in maze
     *
     * @return number of rows
     */
    public int numRows() {
        return numRows;
    }

    /**
     * returns number of columns in maze
     *
     * @return number of columns
     */
    public int numCols() {
        return numCols;
    }




    public boolean isWall(int r1, int c1, int r2, int c2) {
        return isW(new Cell(r1, c1), new Cell(r2, c2));
    }
    /*
    * Member function: isWall
    * Usage: if (maze.isWall(a, b))...
    * ---------------------------------
    * This member function returns true if there is a wall between
    * the two cells at points p1 and p2. If the two points are
    * not neighbors or if either is out of bounds, an error is raised.
    */
    private boolean isW(Cell c1, Cell c2) {
        if (!pointInBounds(c1) || !pointInBounds(c2))
            try {
                throw new Exception("Cell out of bounds!");
            } catch (Exception e) {
                e.printStackTrace();
            }

        boolean res = false;
        boolean neighbours = true;

        if (c1.row == c2.row) {
            if (c1.col - c2.col == 1) {
                res = vertWalls[c1.row][c2.col];
            } else if (c2.col - c1.col == 1) {
                res = vertWalls[c1.row][c1.col];
            } else {
                neighbours = false;
            }
        } else if (c1.col == c2.col) {
            if (c1.row - c2.row == 1) {
                res = horizWalls[c2.row][c1.col];
            } else if (c2.row - c1.row == 1) {
                res = horizWalls[c1.row][c1.col];
            } else {
                neighbours = false;
            }
        } else {
            neighbours = false;
        }

        if (!neighbours){
            try {
                throw new Exception("Cells are not neighbours!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * This member function returns true if p is within bounds of this
     * maze, false otherwise.
     */
    private boolean pointInBounds(Cell c) {
        return (c.row >= 0 && c.row < numRows() && c.col >=0 && c.col < numCols());
    }


    /*
    * Member function: setWall
    * Usage: maze.setWall(a, b, true);
    * --------------------------------
    * This member function sets the wall between cells at points
    * p1 and p2 to state. It can be used to either add or remove
    * walls. The graphical display is updated to match. If the two
    * points are not neighbors or either point is out of bounds,
    * an error is raised.
    */
    private void setWall(Cell c1, Cell c2, boolean state) {
        if (!pointInBounds(c1) || !pointInBounds(c2)) {
            try {
                throw new Exception("Cell out of bounds!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean neighbours = true;

        if (c1.row == c2.row) {
            if (c1.col - c2.col == 1) {
                vertWalls[c1.row][c2.col] = state;
            } else if (c2.col - c1.col == 1) {
                vertWalls[c1.row][c1.col] = state;
            } else {
                neighbours = false;
            }
        } else if (c1.col == c2.col) {
            if (c1.row - c2.row == 1) {
                horizWalls[c2.row][c1.col] = state;
            } else if (c2.row - c1.row == 1) {
                horizWalls[c1.row][c1.col] = state;
            } else {
                neighbours = false;
            }
        } else {
            neighbours = false;
        }

        if (!neighbours){
            try {
                throw new Exception("Cells are not neighbours!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /*
     * Member function: draw
     * Usage: maze.draw();
     * -------------------
     * This member function draws the maze configuration to the graphics
     * window, erasing any previous contents. The lower-left corner is
     * the cell identified by 0-0. The maze itself is white and walls are
     * drawn with black lines. All previous marks are cleared.
     */
    public void draw() {
        System.out.println("---------------------------------------------------------");
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Cell current = new Cell(i, j);
                if (i < numRows-1) {
                    Cell down = new Cell(i + 1, j);
                    if (isW(current, down)) {
                        System.out.print("_");
                    } else {
                        System.out.print(" ");
                    }
                }

                if (j < numCols-1) {
                    Cell right = new Cell(i, j + 1);
                    if (isW(current, right)) {
                        System.out.print("|");
                    } else {
                        System.out.print(" ");
                    }
                }
            }
            System.out.print("\n");
        }
        System.out.println("---------------------------------------------------------");
    }

    public class Cell {
        private int row;
        private int col;

        public Cell(int  row, int col) {
            this.row = row;
            this.col = col;
        }

        public int row() {
            return row;
        }

        public int col() {
            return col;
        }

    }

}






























