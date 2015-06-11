package Game.Model;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import java.util.Random;

/**
 * class explanation @@
 */

public class PlaneMaze {

    private static final int cellWallNum = 4;

    private  Random rand = new Random();


    private CellInner[][] cells;

    private int numRows;
    private int numCols;

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    public PlaneMaze(int numRows, int numCols) {
        rand.setSeed(1);
        try {
            if (numRows < 0 || numCols < 0) {
                throw new Exception("Number of rows and number of columns must be equal or greater than zero!");
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
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    public int numRows() {
        return numRows;
    }


    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    public int numCols() {
        return numCols;
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * @@ maze must be empty, with no walls.
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    public PlaneMaze  makePerfect() { //@@ maybe return this ? fuck me right ?
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

        return this;
    }


    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
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
                        if (cellInBounds(neighbour) && isWall(curr, neighbour)) {
                            setWall(curr, neighbour, false);
                            break;
                        }
                    }
                }
            }
        }

    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
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

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private JsonObjectBuilder createWallJson(Cell p1, Cell p2) {
        JsonObjectBuilder wallJson = Json.createObjectBuilder()
                .add("cell1", Json.createObjectBuilder()
                        .add("row", p1.row)
                        .add("col", p1.col))
                .add("cell2", Json.createObjectBuilder()
                        .add("row", p2.row)
                        .add("col", p2.col));
        return wallJson;
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    public boolean isWall(Cell p1, Cell p2) {
        if (!cellInBounds(p1) || !cellInBounds(p2)) {
            try {
                throw new Exception("Cell out of bounds!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cells[p1.row][p1.col].getWall(neighborDir(p1, p2));
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
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
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    public boolean cellInBounds(Cell p) {
        return (p.row >= 0 && p.row < numRows && p.col >=0 && p.col < numCols);
    }

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private dirEnum neighborDir(Cell c1, Cell c2) {
        if (!Cell.neighbours(c1, c2)) {
            try {
                throw new Exception("Cells are not neighbors!");
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


    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private enum dirEnum{North, East, South, West}

    /**
     * @@ have to rewrite all comments including this ofc
     * awdnaipwn dnawpidnpian wdnanwdpianwpidnpawnd
     * aowdbpanwd pnapwndpanwpdn pawdnapwn danpwdawd
     * awd
     */
    private class CellInner {
        private boolean[] walls;

        private CellInner(int cellWallNum) {
            walls = new boolean[cellWallNum];
        }

        private void setWall(dirEnum d, boolean state) {
            walls[d.ordinal()] = state;
        }

        private boolean getWall(dirEnum d) {
            return walls[d.ordinal()];
        }
    }

}






























