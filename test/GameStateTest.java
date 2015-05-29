import org.junit.Test;

import javax.net.ssl.SSLSession;

import static org.junit.Assert.*;

/**
 * Created by rezo on 5/26/15.
 */
public class GameStateTest {

    @Test
    public void testToString() throws Exception {
        GameState gameState = GameStateStatic.getState();
        gameState.setPlayerById(0,100,33);
        gameState.setPlayerById(1,12,33);
        gameState.setPotion(78,25,0);
        gameState.setPotion(12,12,1);
        gameState.deletePotionIfExists(78,25);
        System.out.println(gameState.toString());
    }
}