import java.util.ArrayList;

/**
 *
 */
public class GameStateStatic {

    private static  GameState gameState  = null;
    private GameStateStatic(){
       gameState = new GameState();
    }
    public static GameState getState(){
        if (gameState==null) {
            new GameStateStatic();
        }
        return gameState;

    }
}
