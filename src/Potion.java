
public class Potion {
    double x;
    double y;
    int id ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Potion potion = (Potion) o;

        double dx = Math.abs(potion.getX() - getX());
        double dy = Math.abs(potion.getY() - getY());
        return dx + dy <= 30 || getId() == potion.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }

    public Potion(double x, double y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
