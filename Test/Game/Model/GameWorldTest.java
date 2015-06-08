package Game.Model;

import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import Game.Model.GameWorld;
import Game.Model.PlaneMaze;
import Game.Model.Player;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by SHAKO on 08-Jun-15.
 */
public class GameWorldTest {

    PlaneMaze pm;

    Map<Player, Boolean> playersOnPos;
    boolean startGame;

    GameWorld gw;
    GameWorld gw1;

    @Before
    public void setUp() throws Exception {
        pm = new PlaneMaze(GameWorld.numRows, GameWorld.numCols);
        pm.makePerfect();

        gw = new GameWorld(pm);

        playersOnPos = new HashMap<>();
        Player p = new Player("killera", true, 0);
        Player p1 = new Player("selapa", true, 0);
        Player p2 = new Player("tekla", true, 0);
        Player p3 = new Player("69slayer69", true, 0);
        playersOnPos.put(p, true);
        playersOnPos.put(p1, true);
        playersOnPos.put(p2, true);
        playersOnPos.put(p3, true);

        startGame = false;

        gw1 = new GameWorld(playersOnPos, pm, startGame);
    }

//    @Test
//    public void testAddPlayerAfterCreateGameWorldBasicAtCorners() throws Exception {
//        // basic player addition at corners
//        Player p = mock(Player.class);
//        when(p.getName()).thenReturn("killera");
//        //when(p.setPosition(anyObject())).the
//
//        Player p1 = mock(Player.class);
//        when(p1.getName()).thenReturn("selapa");
//
//        Player p2 = mock(Player.class);
//        when(p2.getName()).thenReturn("tekla");
//
//        Player p3 = mock(Player.class);
//        when(p3.getName()).thenReturn("69slayer69");
//
//        assert gw.addPlayer(p, true);
//        assert gw.addPlayer(p1, true);
//        assert gw.addPlayer(p2, true);
//        assert gw.addPlayer(p3, true);
//
//        Point2D.Double pos = new Point2D.Double((gw.cellWidth - 2 * GameWorld.pRadius) / 2,
//                (gw.cellHeight - 2 * GameWorld.pRadius) / 2);
//
//        Point2D.Double pos1 = new Point2D.Double((GameWorld.numCols - 1) * gw.cellWidth + (GameWorld.numCols - 1) * GameWorld.wallWidth + (gw.cellWidth - 2 * GameWorld.pRadius) / 2,
//                (gw.cellHeight - 2 * GameWorld.pRadius) / 2);
//
//        Point2D.Double pos2 = new Point2D.Double((GameWorld.numCols - 1) * gw.cellWidth + (GameWorld.numCols - 1) * GameWorld.wallWidth + (gw.cellWidth - 2 * GameWorld.pRadius) / 2,
//                (GameWorld.numRows - 1) * gw.cellHeight + (GameWorld.numRows - 1) * GameWorld.wallWidth + (gw.cellHeight - 2 * GameWorld.pRadius) / 2);
//
//        Point2D.Double pos3 = new Point2D.Double((gw.cellWidth - 2 * GameWorld.pRadius) / 2,
//                (GameWorld.numRows - 1) * gw.cellHeight + (GameWorld.numRows - 1) * GameWorld.wallWidth + (gw.cellHeight - 2 * GameWorld.pRadius) / 2);
//
//        System.out.println("testAddPlayerAfterCreateGameWorld method: take a look at player positions: ");
//        System.out.println(pos);
//        System.out.println(pos1);
//        System.out.println(pos2);
//        System.out.println(pos3);
//
//        assert p.getPosition().equals(pos);
//        assert p1.getPosition().equals(pos1);
//        assert p2.getPosition().equals(pos2);
//        assert p3.getPosition().equals(pos3);
//
//        // reject for one of reasons: already running, same player is in game, reached max count of players.
//
//    }
//
//    @Test
//    public void testAddPlayerAfterCreateGameWorldAtMixedPositions() throws Exception {
//        Player p = new Player("killera", true, 0);
//        Player p1 = new Player("selapa", true, 0);
//        Player p2 = new Player("tekla", true, 0);
//        Player p3 = new Player("69slayer69", true, 0);
//
//        // p1 has valid start position
//
//        // p2 has invalid start position so it will be placed at corner or on random location
//    }

    @Test
    public void testAddPlayersAccordingPassedMapToGameWorld() throws Exception {

    }

    @Test
    public void testAddPlayer1() throws Exception {

    }

    @Test
    public void testStartGame() throws Exception {

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
