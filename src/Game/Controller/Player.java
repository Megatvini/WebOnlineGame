package Game.Controller;

/**
 * Created by Nika on 00:17, 5/25/2015.
 */
public class Player {
    private int x, y;
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        if (x<=0 || x>=300) return;
        this.x = x;
    }

    public void setY(int y) {
        if (y<=0 || y>=200) return;
        this.y = y;
    }

    @Override
    public String toString() {
        String res = "";
        res = x + ":" + y;
        return res;
    }
}
