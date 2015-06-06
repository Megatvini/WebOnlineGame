package Game.Model;

import java.awt.*;
import java.util.*;

/**
 * class explanation @@
 */

public class PlaneMaze {

    private static int cellWallNum = 4;

    private Random rand = new Random();

    //
    private boolean[][] horizWalls;
    private  boolean[][] vertWalls;

    private int numRows;
    private int numCols;

    /**
     *
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
                    if (isW(new Point(i, j), new Point(i - 1, j))) {
                        conn[j] = i * numCols + j;
                    }
                }
            }

            for (int j = 0; j < numCols; j++) {

                Point curr = new Point(i, j);

                if (i != numRows - 1) {
                    if (j < numCols - 1) {
                        if (conn[j] == conn[j + 1] || rand.nextDouble() >= 0.5) {
                            setWall(curr, new Point(i, j + 1), true);
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
                        setWall(curr, new Point(i, j + 1), true);
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
                Point curr = new Point(i, j);

                if (i < numRows - 1) {
                    if (j < numCols - 1) {
                        if (conn[j] == conn[j + 1]) {
                            bottom++;
                            if (bottom > 1 && rand.nextDouble() >= 0.3) {
                                setWall(curr, new Point(i + 1, j), true);
                                bottom--;
                            }
                        } else {
                            if (bottom > 1 && rand.nextDouble() >= 0.3) {
                                setWall(curr, new Point(i + 1, j), true);
                                bottom--;
                            }
                            bottom = 1;
                        }
                    } else {
                        if (bottom > 1 && rand.nextDouble() >= 0.3) {
                            setWall(curr, new Point(i + 1, j), true);
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
                    Point curr = new Point(i, j);
                    Point neighbour = null;
                    for (int k = 0; k < cellWallNum; k++) {
                        switch (k) {
                            case 0:
                                neighbour = new Point(i - 1, j);
                                break;
                            case 1:
                                neighbour = new Point(i, j + 1);
                                break;
                            case 2:
                                neighbour = new Point(i + 1, j);
                                break;
                            case 3:
                                neighbour = new Point(i, j - 1);
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
        return isW(new Point(r1, c1), new Point(r2, c2));
    }

    /**
     * checks weather there is wall between certain neighbour cells, if cells are not
     * neighbours of either of them is out of bounds proper exceptions thrown
     * @param p1 first cell represented as point, x - row, y - col
     * @param p2 second cell represented as point, x - row, y - col
     * @return true if there is wall between given cells, false otherwise
     * @exception Exception if either of cells is out of bounds or if they are not adjacent
     */
    private boolean isW(Point p1, Point p2) {
        if (!pointInBounds(p1) || !pointInBounds(p2))
            try {
                throw new Exception("Cell out of bounds!");
            } catch (Exception e) {
                e.printStackTrace();
            }

        boolean res = false;
        boolean neighbours = true;

        if (p1.x == p2.x) {
            if (p1.y - p2.y == 1) {
                res = vertWalls[p1.x][p2.y];
            } else if (p2.y - p1.y == 1) {
                res = vertWalls[p1.x][p1.y];
            } else {
                neighbours = false;
            }
        } else if (p1.y == p2.y) {
            if (p1.x - p2.x == 1) {
                res = horizWalls[p2.x][p1.y];
            } else if (p2.x - p1.x == 1) {
                res = horizWalls[p1.x][p1.y];
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
    public boolean pointInBounds(Point c) {
        return (c.x >= 0 && c.x < numRows() && c.y >=0 && c.y < numCols());
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
    private void setWall(Point p1, Point p2, boolean state) {
        if (!pointInBounds(p1) || !pointInBounds(p2)) {
            try {
                throw new Exception("Cell out of bounds!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!neighbours(p1, p2)) {
            try {
                throw new Exception("Cells are not neighbours!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (p1.y == p1.y) {
           // horizWalls
        }

    }

    private boolean neighbours(Point p1, Point p2){
        if (p1.y == p2.y) {
            if (Math.abs(p1.x - p2.x) == 1) {
                return true;
            } else {
                return false; // avoid unnecessary checking
            }
        } else if (p1.x == p2.y) {
            if (Math.abs(p1.x - p2.x) == 1) {
                return true;
            }
        }
        return false;
    }

    // es ar gamoiyeno
    public String toString() {
        String res = "";

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Point current = new Point(i, j);
                if (i < numRows - 1) {
                    Point down = new Point(i + 1, j);
                    if (isW(current, down)) {
                        if (res !="") {
                            res += "#";
                        }
                        res += current.x + ":" + current.y + "#" + down.x + ":" + down.y;
                    }
                }

                if (j < numCols - 1) {
                    Point right = new Point(i, j + 1);
                    if (isW(current, right)) {
                        if (res !="") {
                            res += "#";
                        }
                        res += current.x + ":" + current.y + "#" + right.x + ":" + right.y;
                    }
                }
            }
        }

        return res;
    }

    public class bla {
        public void kla() {

        }
    }

}






























