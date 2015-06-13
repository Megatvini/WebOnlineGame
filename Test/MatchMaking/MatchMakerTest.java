package MatchMaking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Nika on 13:10, 6/11/2015.
 */
public class MatchMakerTest {
    @Test
    public void testAddParticipant() throws Exception {
        Map<String, Collection<String>> roomMates = new HashMap<>();
        MatchMaker matchMaker = new MatchingManager(roomMates, new FixedRoomSizeMatcherFactory());

        Set<Integer> set = new HashSet<>();
        set.add(2);
        matchMaker.addParticipant("player1", set);
        matchMaker.addParticipant("player2", set);

        assertEquals(2, roomMates.size());
    }

    @Test
    public void testAddParticipant3() throws Exception {
        Map<String, Collection<String>> roomMates = new HashMap<>();
        MatchMaker matchMaker = new MatchingManager(roomMates, new FixedRoomSizeMatcherFactory());

        Set<Integer> set = new HashSet<>();
        set.add(3);
        matchMaker.addParticipant("player1", set);
        matchMaker.addParticipant("player2", set);

        assertEquals(0, roomMates.size());
    }


    @Test
    public void testAddParticipant1() throws Exception {
        Map<String, Collection<String>> roomMates = new HashMap<>();
        MatchMaker matchMaker = new MatchingManager(roomMates, new FixedRoomSizeMatcherFactory());

        Set<Integer> set = new HashSet<>();
        Set<Integer> set1 = new HashSet<>();
        set.add(2);
        set.add(3);
        set.add(4);
        set1.add(3);

        matchMaker.addParticipant("player1", set);
        matchMaker.addParticipant("player2", set1);

        assertEquals(0, roomMates.size());

        Set<Integer> set3 = new HashSet<>();
        set3.add(3);
        matchMaker.addParticipant("player3", set3);
        assertEquals(3, roomMates.size());
        assertTrue(roomMates.containsKey("player1"));
        assertTrue(roomMates.containsKey("player2"));
        assertTrue(roomMates.containsKey("player3"));
        assertTrue(roomMates.get("player1") == roomMates.get("player2"));
        assertTrue(roomMates.get("player2") == roomMates.get("player3"));
    }

    @Test
    public void testAddParticipants1() throws Exception {
        Map<String, Collection<String>> roomMates = new HashMap<>();
        MatchMaker matchMaker = new MatchingManager(roomMates, new FixedRoomSizeMatcherFactory());

        Set<Integer> sizes = new HashSet<>();
        Set<String> players = new HashSet<>();

        sizes.add(2);
        players.add("p1");
        players.add("p2");
        matchMaker.addParticipants(players, sizes);

        assertEquals(2, roomMates.size());
        assertTrue(roomMates.containsKey("p1"));
        assertTrue(roomMates.containsKey("p2"));
        assertTrue(roomMates.get("p1") == roomMates.get("p2"));
    }

    @Test
    public void testAddParticipants2() throws Exception {
        Map<String, Collection<String>> roomMates = new HashMap<>();
        MatchMaker matchMaker = new MatchingManager(roomMates, new FixedRoomSizeMatcherFactory());

        Set<Integer> sizes1 = new HashSet<>();
        Set<String> players1 = new HashSet<>();
        Set<Integer> sizes2 = new HashSet<>();
        Set<String> players2 = new HashSet<>();

        sizes1.add(3);
        sizes1.add(4);
        players1.add("p1");
        players1.add("p2");

        sizes2.add(4);
        players2.add("p3");
        players2.add("p4");

        matchMaker.addParticipants(players1, sizes1);
        assertEquals(0, roomMates.size());

        matchMaker.addParticipants(players2, sizes2);

        assertEquals(4, roomMates.size());
        assertTrue(roomMates.containsKey("p1"));
        assertTrue(roomMates.containsKey("p2"));
        assertTrue(roomMates.containsKey("p3"));
        assertTrue(roomMates.containsKey("p4"));
        assertTrue(roomMates.get("p1") == roomMates.get("p2"));
        assertTrue(roomMates.get("p2") == roomMates.get("p3"));
        assertTrue(roomMates.get("p3") == roomMates.get("p4"));
    }


    @Test
    public void testAddParticipants3() throws Exception {
        Map<String, Collection<String>> roomMates = new HashMap<>();
        MatchMaker matchMaker = new MatchingManager(roomMates, new FixedRoomSizeMatcherFactory());

        Set<Integer> sizes1 = new HashSet<>();
        Set<String> players1 = new HashSet<>();
        Set<Integer> sizes2 = new HashSet<>();
        Set<String> players2 = new HashSet<>();

        sizes1.add(3);
        players1.add("p1");
        players1.add("p2");

        sizes2.add(3);
        players2.add("p3");
        players2.add("p4");

        matchMaker.addParticipants(players1, sizes1);
        assertEquals(0, roomMates.size());
        matchMaker.addParticipants(players2, sizes2);
        assertEquals(0, roomMates.size());
    }
}