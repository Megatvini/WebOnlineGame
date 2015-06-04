package Game.Model;

import java.awt.geom.Point2D;

/**
 * Created by SHAKO on 03-Jun-15.
 */
public class Player {//@@ aq radius statikurad wavikitxo configedan

    private String name;
    private  double x;
    private double y;
    private double radius;
    private boolean active;

    private int potionNum;

    public Player(String name, double x, double y, double radius, boolean active) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.active = active;

        potionNum = 0;
    }

    public String getName() {
        return name;
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

    public double getRadius() {
        return radius;
    }

    public void potionPlus() {
        potionNum++;
    }

    public void setPotNum(int potNum) {
        potionNum = potNum;
    }

    public int getPotNum() {
        return potionNum;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }

}
