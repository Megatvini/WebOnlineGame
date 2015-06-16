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

    private  double x;
    private double y;

    public Player(String name){
        this(name, true, 0);
    }

    public Player(String name, boolean active, int potNum) {
        this.name = name;
        this.active = active;
        this.potNum = potNum;
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
        if (potNum < 0) {
            throw new RuntimeException("Potion number must be greater or equal to zero!");
        }
        this.potNum = potNum;
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

    @Override
    public boolean equals(Object player) {
        Player p;
        if (player != null && player instanceof Player) {
            p = (Player)player;
        } else {
            return false;
        }
        if (this == p) {
            return true;
        }
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
