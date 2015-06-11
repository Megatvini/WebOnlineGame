package Game.Model;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.awt.geom.Point2D;

/**
 * Created by SHAKO on 03-Jun-15.
 */
public class Player {

    private String name;

    private boolean active;

    private int potNum;

    public enum StartCellT {atCorner, given, random}
    private StartCellT sct;
    private Cell startCell;
    public static final Cell defStartCell = new Cell(0, 0);

    private  double x;
    private double y;

    public Player(String name){
        this(name, true, 0, StartCellT.atCorner);
    }

    public Player(String name, boolean active, int potNum, Cell startCell) {
        this(name, active, potNum, StartCellT.given);
        setStartCell(startCell);
    }

    public Player(String name, boolean active, int potNum, StartCellT sct) {
        this.name = name;
        this.active = active;
        this.potNum = potNum;
        this.sct = sct;
        if (this.sct == StartCellT.given)
            startCell = defStartCell;
    }


    public String getName() {
        return name;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public int getPotNum() {
        return potNum;
    }

    public void potionPlus() {
        potNum++;
    }

    //@@ exception aris aq setPotNum-shi
    public void setPotNum(int potNum) {
        try {
            if (potNum < 0) {
                throw new Exception("Potion number must be greater or equal to zero!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.potNum = potNum;
    }

    public StartCellT getStartCellT() {
        return sct;
    }

    public void setStartCellT(StartCellT sct) {
        this.sct = sct;
        if (this.sct == StartCellT.given && startCell == null) {
            startCell = defStartCell;
        }
    }

    public Cell getStartCell() {
        return startCell;
    }

    /**
     * @@ take effect if only start cell type if given
     * @param startCell
     */
    public void setStartCell(Cell startCell) {
        if (sct == StartCellT.given) {
            this.startCell = startCell;
        }
    }

    public Point2D.Double getPosition() {
        return new Point2D.Double(x, y);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(Point2D.Double loc) {
        x = loc.x;
        y = loc.y;
    }

    public boolean equals(Player p) {
        return name.equals(p.getName());
    }

    public JsonObjectBuilder toJsonBuilder() {
        JsonObjectBuilder playerJson = Json.createObjectBuilder();

        playerJson.add("active", active);

        playerJson.add("name", name);

        JsonObjectBuilder plPosJson = Json.createObjectBuilder();

        plPosJson.add("x", x).add("y", y);

        playerJson.add("position", plPosJson);

        return playerJson;
    }

}
