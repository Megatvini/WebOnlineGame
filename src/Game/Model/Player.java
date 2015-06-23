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

    private int lifeNum;

    private int potNum;

    private  double x;
    private double y;

    private int type;

    int place;

    public Player(String name, int lifeNum, int type){
        this(true, name, lifeNum, 0, type);
    }

    public Player(boolean active, String name, int lifeNum, int potNum, int type) {
        this.active = active;
        this.name = name;
        this.lifeNum = lifeNum;
        this.potNum = potNum;
        this.type = type;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public int getLifeNum() {
        return lifeNum;
    }

    public void decreaseLifeNum() {
        setLifeNum(lifeNum - 1);
    }

    public void setLifeNum(int lifeNum) {
        this.lifeNum = lifeNum;
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

    public boolean isWinner(Player enemy) {
        if (potNum > enemy.potNum) {
            return true;
        } else if (enemy.potNum > potNum) {
            return false;
        }else if (rand.nextBoolean()) {
            return true;
        } else {
            return false;
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