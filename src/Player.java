//import com.google.gson.Gson;
//import com.google.gson.annotations.Expose;
//
//
//public class Player {
//    @Expose
//    private double x;
//    @Expose
//    private double y;
//    @Expose
//    private int id ;
//
//
//
//
//
//    public Player(int id, double x, double y) {
//        this.id = id;
//        this.y = y;
//        this.x = x;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public double getX() {
//        return x;
//    }
//
//    public void setX(double x) {
//        this.x = x;
//    }
//
//    public double getY() {
//        return y;
//    }
//
//    public void setY(double y) {
//        this.y = y;
//    }
//
//
//    @Override
//    public String toString() {
//        return new Gson().toJson(this);
//    }
//
//    @Override
//    public int hashCode() {
//        int result;
//        long temp;
//        temp = Double.doubleToLongBits(x);
//        result = (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(y);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        result = 31 * result + id;
//        return result;
//    }
//}
