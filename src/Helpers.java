import java.util.Random;


public class Helpers {
   static  Random rand = new Random();
    public static double randDouble(double min, double max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.


        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return  min + (max - min) * rand.nextDouble();
    }
}
