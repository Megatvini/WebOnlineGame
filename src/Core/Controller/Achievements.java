package Core.Controller;

/**
 * Created by Annie on 26-Jun-15.
 */
public class Achievements {
    public static enum Names{MDZLETAMDZLE, CHAINIKI};

    public static Names getName(int raiting){
        if(raiting > 1000) return  Names.CHAINIKI;
        else return  Names.MDZLETAMDZLE;
    }
}