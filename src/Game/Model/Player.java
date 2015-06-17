package Game.Model;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Created by SHAKO on 03-Jun-15.
 */
public class Player {
    private static Random rand = new Random();

    private boolean active;

    private String name;

    private int potNum;
    
    private  double x;
    private double y;

    private int type;

    int place;

    public Player(String name, int type){
        this(name, true, 0, type);
    }

    public Player(String name, boolean active, int potNum, int type) {
        this.name = name;
        this.active = active;
        this.potNum = potNum;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getPlace() {
        return place;
    }

    public static Player getWinner(Player p1, Player p2) {
        if (p1.potNum > p2.potNum) {
            return p1;
        } else if (p2.potNum > p1.potNum) {
            return p2;
        }else if (rand.nextBoolean()) {
            return p1;
        } else {
            return p2;
        }
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
