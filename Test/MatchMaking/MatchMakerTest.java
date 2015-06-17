package MatchMaking;


import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void containsParticipantTest() {
        Map<String, Collection<String>> roomMates = new HashMap<>();
        MatchMaker matchMaker = new MatchingManager(roomMates, new FixedRoomSizeMatcherFactory());

        Set<Integer> sizes1 = new HashSet<>();
        Set<String> players1 = new HashSet<>();
        Set<Integer> sizes2 = new HashSet<>();
        Set<String> players2 = new HashSet<>();

        sizes1.add(3);
        players1.add("p1");
        players1.add("p2");

        matchMaker.addParticipants(players1, sizes1);
        assertEquals(0, roomMates.size());

        assertTrue(matchMaker.containsParticipant("p1"));
        assertTrue(matchMaker.containsParticipant("p2"));
        assertFalse(matchMaker.containsParticipant("p3"));
        assertFalse(matchMaker.containsParticipant("asd"));

        sizes2.add(3);
        players2.add("p3");
        players2.add("p4");

        matchMaker.addParticipants(players2, sizes2);
        assertEquals(0, roomMates.size());

        assertTrue(matchMaker.containsParticipant("p1"));
        assertTrue(matchMaker.containsParticipant("p2"));
        assertTrue(matchMaker.containsParticipant("p3"));
        assertTrue(matchMaker.containsParticipant("p4"));
    }


    @Test
    public void containsParticipantTest2() {
        Map<String, Collection<String>> roomMates = new HashMap<>();
        MatchMaker matchMaker = new MatchingManager(roomMates, new FixedRoomSizeMatcherFactory());

        Set<Integer> sizes1 = new HashSet<>();
        Set<String> players1 = new HashSet<>();
        Set<Integer> sizes2 = new HashSet<>();
        Set<String> players2 = new HashSet<>();

        sizes1.add(4);
        players1.add("p1");
        players1.add("p2");

        matchMaker.addParticipants(players1, sizes1);
        assertEquals(0, roomMates.size());

        assertTrue(matchMaker.containsParticipant("p1"));
        assertTrue(matchMaker.containsParticipant("p2"));
        assertFalse(matchMaker.containsParticipant("p3"));
        assertFalse(matchMaker.containsParticipant("asd"));

        sizes2.add(4);
        players2.add("p3");
        players2.add("p4");

        matchMaker.addParticipants(players2, sizes2);
        assertEquals(4, roomMates.size());

        assertTrue(matchMaker.containsParticipant("p1"));
        assertTrue(matchMaker.containsParticipant("p2"));
        assertTrue(matchMaker.containsParticipant("p3"));
        assertTrue(matchMaker.containsParticipant("p4"));
    }

    @Test
    public void containsParticipantTest3() {
        Map<String, Collection<String>> roomMates = new HashMap<>();
        MatchMaker matchMaker = new MatchingManager(roomMates, new FixedRoomSizeMatcherFactory());

        Set<Integer> sizes1 = new HashSet<>();
        Set<String> players1 = new HashSet<>();
        Set<Integer> sizes2 = new HashSet<>();
        Set<String> players2 = new HashSet<>();

        sizes1.add(4);
        players1.add("p1");
        players1.add("p2");

        matchMaker.addParticipants(players1, sizes1);
        assertEquals(0, roomMates.size());

        assertTrue(matchMaker.containsParticipant("p1"));
        assertTrue(matchMaker.containsParticipant("p2"));
        assertFalse(matchMaker.containsParticipant("p3"));
        assertFalse(matchMaker.containsParticipant("asd"));

        sizes2.add(4);
        players2.add("p3");
        players2.add("p4");

        matchMaker.addParticipants(players2, sizes2);
        assertEquals(4, roomMates.size());

        roomMates.remove("p1");
        roomMates.remove("p2");
        roomMates.remove("p3");

        assertFalse(matchMaker.containsParticipant("p1"));
        assertFalse(matchMaker.containsParticipant("p2"));
        assertFalse(matchMaker.containsParticipant("p3"));
        assertTrue(matchMaker.containsParticipant("p4"));
    }

    @Test
    public void combinedTest() {
        Map<String, Collection<String>> roomMates = new HashMap<>();
        MatchMaker matchMaker = new MatchingManager(roomMates, new FixedRoomSizeMatcherFactory());

        Set<Integer> sizes1 = new HashSet<>();
        Set<Integer> sizes2 = new HashSet<>();

        sizes1.add(2);
        sizes1.add(3);
        sizes1.add(4);

        sizes2.add(2);
        sizes2.add(3);

        matchMaker.addParticipant("p1", sizes1);
        matchMaker.addParticipant("p2", sizes2);

        assertEquals(2, roomMates.size());


        Set<Integer> sizes3 = new HashSet<>();
        Set<Integer> sizes4 = new HashSet<>();

        sizes3.add(3);
        sizes3.add(4);


        sizes4.add(3);
        sizes4.add(4);

        matchMaker.addParticipant("p3", sizes3);
        matchMaker.addParticipant("p4", sizes4);

        System.out.println(roomMates);
        assertEquals(2, roomMates.size());
    }

    @Test
    public void behaviorTest() {
        Map<String, Collection<String>> roomMates = new HashMap<>();

        FixedRoomSizeMatcherFactory factoryMock = mock(FixedRoomSizeMatcherFactory.class);
        FixedRoomSizeMatcher matcherMockSize2 = mock(FixedRoomSizeMatcher.class);
        FixedRoomSizeMatcher matcherMockSize3 = mock(FixedRoomSizeMatcher.class);
        FixedRoomSizeMatcher matcherMockSize4 = mock(FixedRoomSizeMatcher.class);

        when(factoryMock.getInstance(2)).thenReturn(matcherMockSize2);
        when(factoryMock.getInstance(3)).thenReturn(matcherMockSize3);
        when(factoryMock.getInstance(4)).thenReturn(matcherMockSize4);

        MatchMaker matchMaker = new MatchingManager(roomMates, factoryMock);

        verify(factoryMock).getInstance(2);
        verify(factoryMock).getInstance(3);
        verify(factoryMock).getInstance(4);


        when(matcherMockSize2.addNewPlayerGroup(any())).thenReturn(null);
        when(matcherMockSize3.addNewPlayerGroup(any())).thenReturn(null);
        when(matcherMockSize4.addNewPlayerGroup(any())).thenReturn(null);

        Set<Integer> sizes1 = new HashSet<>();
        Set<Integer> sizes2 = new HashSet<>();

        sizes1.add(2);
        sizes1.add(3);
        sizes1.add(4);

        sizes2.add(2);
        sizes2.add(3);

        matchMaker.addParticipant("p1", sizes1);
        matchMaker.addParticipant("p2", sizes2);

        verify(matcherMockSize2, times(2)).addNewPlayerGroup(any());
        verify(matcherMockSize3, times(2)).addNewPlayerGroup(any());
        verify(matcherMockSize4).addNewPlayerGroup(any());
    }

    @Test
    public void behaviorTestRemove() {
        Map<String, Collection<String>> roomMates = new HashMap<>();

        FixedRoomSizeMatcherFactory factoryMock = mock(FixedRoomSizeMatcherFactory.class);
        FixedRoomSizeMatcher matcherMockSize2 = mock(FixedRoomSizeMatcher.class);
        FixedRoomSizeMatcher matcherMockSize3 = mock(FixedRoomSizeMatcher.class);
        FixedRoomSizeMatcher matcherMockSize4 = mock(FixedRoomSizeMatcher.class);

        when(factoryMock.getInstance(2)).thenReturn(matcherMockSize2);
        when(factoryMock.getInstance(3)).thenReturn(matcherMockSize3);
        when(factoryMock.getInstance(4)).thenReturn(matcherMockSize4);

        MatchMaker matchMaker = new MatchingManager(roomMates, factoryMock);


        Set<Set<String>> res = new HashSet<>();
        Set<String> group1 = new HashSet<>();
        group1.add("g1p1");
        group1.add("g1p2");

        Set<String> group2 = new HashSet<>();
        group2.add("g2p1");
        group2.add("g2p2");

        res.add(group1);
        res.add(group2);

        when(matcherMockSize4.addNewPlayerGroup(any())).thenReturn(null).thenReturn(res);

        Set<Integer> sizes1 = new HashSet<>();
        Set<Integer> sizes2 = new HashSet<>();

        sizes1.add(4);
        sizes2.add(4);

        matchMaker.addParticipants(group1, sizes1);
        matchMaker.addParticipants(group2, sizes2);

        verify(matcherMockSize4, times(2)).addNewPlayerGroup(any());
        verify(matcherMockSize4).removePlayerGroup(group1);
        verify(matcherMockSize4).removePlayerGroup(group2);
    }
}