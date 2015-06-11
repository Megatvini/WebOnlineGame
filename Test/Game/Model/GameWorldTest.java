package Game.Model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by SHAKO on 11-Jun-15.
 */
public class GameWorldTest {

    PlaneMaze pm;
    ArrayList<Player> players;
    String pName = "Killera";
    String p1Name  = "Selapa";
    String p2Name = "tekla";
    String p3Name = "69slayer69";
    boolean startGame;

    GameWorld gw;
    GameWorld gw1;

    @Before
    public void setUp() throws Exception {
        pm = new PlaneMaze(GameWorld.numRows, GameWorld.numCols);
        pm.makePerfect();

        gw = new GameWorld(pm);

        players = new ArrayList<>();
        Player p = new Player(pName);
        Player p1 = new Player(p1Name);
        Player p2 = new Player(p2Name);
        Player p3 = new Player(p3Name);
        players.add(p);
        players.add(p1);
        players.add(p2);
        players.add(p3);

        startGame = false;

        gw1 = new GameWorld(players, pm, startGame);
    }

    @Test
    public void testConstructors() throws Exception {
        assertFalse(gw.gameOn());
        assert gw.getPlayers().size() == 0;

        Collection<String> playerNames = gw1.getPlayerNames();
        assert playerNames.size() == 4;
        assert playerNames.contains(pName);
        assert playerNames.contains(p1Name);
        assert playerNames.contains(p2Name);
        assert playerNames.contains(p3Name);
    }

    @Test
    public void testAddPlayerWithStringName() throws Exception {
        for (int i = 0; i < GameWorld.maxPlayers; i++) {
            assert gw.addPlayer(Integer.toString(i));
        }
        assert !gw.addPlayer("Exstra");

        for (int i = 0; i < GameWorld.maxPlayers; i++) {
            assert gw.getPlayerNames().contains(Integer.toString(i));
        }
    }

    @Test
    public void testAddPlayerWithObjectPlayer() throws Exception {
        for (int i = 0; i < GameWorld.maxPlayers; i++) {
            Player p = new Player(Integer.toString(i));
            assert gw.addPlayer(p);
        }

        assert !gw.addPlayer(new Player("exstra"));

        for (int i = 0; i < GameWorld.maxPlayers; i++) {
            assert gw.getPlayerNames().contains(Integer.toString(i));
        }
    }

    @Test
    public void testStartGame() throws Exception {
        GameWorld gwMock = mock(GameWorld.class);
        when(gwMock.gameOn()).thenReturn(false);


    }

    @Test
    public void testAddPotAtRand() throws Exception {

    }

    @Test
    public void testAddPotInCell() throws Exception {

    }

    @Test
    public void testPlayerMove() throws Exception {

    }

    @Test
    public void testSetPlayerCoordinates() throws Exception {

    }

    @Test
    public void testNumberOfPlayers() throws Exception {

    }

    @Test
    public void testGetPlayerNames() throws Exception {

    }

    @Test
    public void testGetPlayers() throws Exception {

    }

    @Test
    public void testGameOn() throws Exception {

    }

    @Test
    public void testGetInit() throws Exception {

    }

    @Test
    public void testGetUpdate() throws Exception {

    }

    @Test
    public void testFinishGame() throws Exception {

    }
}