package Core.Bean;

import org.junit.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Nika on 03:40, 6/27/2015.
 */
public class GameTest {

    @Test
    public void testGetDate() throws Exception {
        Date date = new Date(System.currentTimeMillis());
        Game game = new Game(date, 10, 20);
        assertEquals(date, game.getDate());
        Date date2 = new Date(System.currentTimeMillis());
        game.setDate(date2);
        assertEquals(date2, game.getDate());
    }

    @Test
    public void testGameID() throws Exception {
        Date date = new Date(System.currentTimeMillis());
        Game game = new Game(date, 10, 20);
        assertEquals(10, game.getGameID());
        game.setGameID(12);
        assertEquals(12, game.getGameID());
    }

    @Test
    public void testRatingChange() throws Exception {
        Date date = new Date(System.currentTimeMillis());
        Game game = new Game(date, 10, 20);
        assertEquals(20, game.getRatingChange());
        game.setRatingChange(50);
        assertEquals(50, game.getRatingChange());
    }

    @Test
    public void testAddParticipant() throws Exception {
        Date date = new Date(System.currentTimeMillis());
        Game game = new Game(date, 10, 20);
        game.addParticipant(1);
        game.addParticipant(2);
        assertEquals(2, game.getParticipantIDs().size());
        Set<Integer> set = game.getParticipantIDs();
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
    }

    @Test
    public void testRemoveParticipant() throws Exception {
        Date date = new Date(System.currentTimeMillis());
        Game game = new Game(date, 10, 20);
        game.addParticipant(1);
        game.addParticipant(2);
        game.addParticipant(2);
        game.addParticipant(2);
        assertEquals(2, game.getParticipantIDs().size());
        Set<Integer> set = game.getParticipantIDs();
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));

        game.removeParticipant(2);
        assertEquals(1, game.getParticipantIDs().size());
        set = game.getParticipantIDs();
        assertFalse(set.contains(2));
        assertTrue(set.contains(1));
    }

}