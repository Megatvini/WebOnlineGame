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

    private int type;

    private int place;

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

    public synchronized boolean getActive() {
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    public synchronized int getPotNum() {
        return potNum;
    }

    public synchronized void potionPlus() {
        potNum++;
    }

    //@@ exception aris aq setPotNum-shi
    public synchronized void setPotNum(int potNum) {
        if (potNum < 0) {
            throw new RuntimeException("Potion number must be greater or equal to zero!");
        }
        this.potNum = potNum;
    }

    public int getType() {
        return type;
    }

    public synchronized void setPlace(int place) {
        this.place = place;
    }

    public synchronized int getPlace() {
        return place;
    }

    public static synchronized Player getWinner(Player p1, Player p2) {
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

    public synchronized JsonObjectBuilder toJsonBuilder() {
        JsonObjectBuilder playerJson = Json.createObjectBuilder();

        playerJson.add("active", active);

        playerJson.add("name", name);

        return playerJson;
    }

}
