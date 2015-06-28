package Core.Controller;

/**
 * Created by Annie on 26-Jun-15.
 */
public class Achievements {

    public static String getName(int raiting){
        if ( raiting > 5000)
            return  "4.png";
        else  if ( raiting > 3000)
            return  "3.png";
        else  if ( raiting > 1000)
            return  "2.png";
        else  if ( raiting > 700)
            return  "1.png";
        else  if ( raiting > 400)
            return  "0.png";
        else  if ( raiting > 300)
            return  "-1.png";
        else  if ( raiting > 200)
            return  "-2.png";
        else  if ( raiting > 100)
            return  "-3.png";
        else
            return  "-4.png";

    }
}
