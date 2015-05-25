import java.util.ArrayList;

/**
 *
 */
public class GameStateStatic {

    private static  GameState gameState  = null;
    private GameStateStatic(){
       gameState = new GameState(new ArrayList<>());
    }
    public static GameState getState(){
        if (gameState==null) {
            new GameStateStatic();
        }
        return gameState;

    }
}
