package MatchMaking;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Nika on 18:45, 6/11/2015.
 */
public class FixedRoomSizeMatcherTest {

    @Test
    public void testBasic2Case() {
        FixedRoomSizeMatcher matcher = new FixedRoomSizeMatcher(2);
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("nika", 0);
        map1.put("guram", 0);

        Set<Set<String>> res = matcher.addNewPlayerGroup(map1);
        assertFalse(res == null);

        Set<String> union = flatten(res);
        assertEquals(2, union.size());
        assertTrue(union.contains("nika"));
        assertTrue(union.contains("guram"));
    }

    @Test
    public void testBasic2Case1() {
        FixedRoomSizeMatcher matcher = new FixedRoomSizeMatcher(2);
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        map1.put("nika", 0);
        map2.put("guram", 0);

        assertEquals(null, matcher.addNewPlayerGroup(map1));

        Set<Set<String>> res = matcher.addNewPlayerGroup(map2);
        assertFalse(res == null);


        Set<String> union = flatten(res);
        assertEquals(2, union.size());
        assertTrue(union.contains("nika"));
        assertTrue(union.contains("guram"));
    }

    @Test
    public void testBasic3Case() {
        FixedRoomSizeMatcher matcher = new FixedRoomSizeMatcher(3);
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        map1.put("nika", 0);
        map2.put("guram", 0);

        assertEquals(null, matcher.addNewPlayerGroup(map1));
        assertEquals(null, matcher.addNewPlayerGroup(map2));

    }

    @Test
    public void testBasic3Case1() {
        FixedRoomSizeMatcher matcher = new FixedRoomSizeMatcher(3);
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        Map<String, Integer> map3 = new HashMap<>();
        map1.put("nika", 0);
        map2.put("guram", 0);
        map3.put("joni", 0);

        assertEquals(null, matcher.addNewPlayerGroup(map1));
        assertEquals(null, matcher.addNewPlayerGroup(map2));

        Set<Set<String>> res = matcher.addNewPlayerGroup(map3);
        assertFalse(res == null);

        Set<String> union = flatten(res);
        assertEquals(3, union.size());
        assertTrue(union.contains("nika"));
        assertTrue(union.contains("guram"));
        assertTrue(union.contains("joni"));
    }

    @Test
    public void Test4() {
        FixedRoomSizeMatcher matcher = new FixedRoomSizeMatcher(3);
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        map1.put("nika", 0);
        map2.put("guram", 0);
        map2.put("joni", 0);

        assertEquals(null, matcher.addNewPlayerGroup(map1));

        Set<Set<String>> res = matcher.addNewPlayerGroup(map2);
        assertFalse(res == null);

        Set<String> union = flatten(res);
        assertEquals(3, union.size());
        assertTrue(union.contains("nika"));
        assertTrue(union.contains("guram"));
        assertTrue(union.contains("joni"));
    }

    @Test
    public void CombinedTest() {
        FixedRoomSizeMatcher matcher = new FixedRoomSizeMatcher(3);
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        Map<String, Integer> map3 = new HashMap<>();
        map1.put("p1", 0);
        map1.put("p2", 0);
        map2.put("p3", 0);
        map2.put("p4", 0);
        map3.put("p5", 0);

        assertEquals(null, matcher.addNewPlayerGroup(map1));
        assertEquals(null, matcher.addNewPlayerGroup(map2));

        Set<Set<String>> res = matcher.addNewPlayerGroup(map3);
        assertFalse(res == null);

        Set<String> union = flatten(res);
        assertEquals(3, union.size());
        assertTrue(union.contains("p5"));
    }

    @Test
    public void testDifferentRatings() {
        FixedRoomSizeMatcher matcher = new FixedRoomSizeMatcher(3);
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        Map<String, Integer> map3 = new HashMap<>();
        map1.put("p1", 200);
        map1.put("p2", 200);
        map2.put("p3", 20);
        map2.put("p4", 20);
        map3.put("p5", 50);

        assertEquals(null, matcher.addNewPlayerGroup(map1));
        assertEquals(null, matcher.addNewPlayerGroup(map2));

        Set<Set<String>> res = matcher.addNewPlayerGroup(map3);
        assertFalse(res == null);

        Set<String> union = flatten(res);
        assertEquals(3, union.size());
        assertTrue(union.contains("p5"));
        assertTrue(union.contains("p3"));
        assertTrue(union.contains("p4"));
    }


    @Test
    public void testRemoveGroup() {
        FixedRoomSizeMatcher matcher = new FixedRoomSizeMatcher(3);
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        Map<String, Integer> map3 = new HashMap<>();

        map1.put("p1", 200);
        map1.put("p2", 300);
        map2.put("p3", 20);
        map2.put("p4", 30);
        map3.put("p5", 100);

        assertEquals(null, matcher.addNewPlayerGroup(map1));
        assertEquals(null, matcher.addNewPlayerGroup(map2));
        matcher.removePlayerGroup(map2.keySet());

        Set<Set<String>> res = matcher.addNewPlayerGroup(map3);
        assertFalse(res == null);

        Set<String> union = flatten(res);
        assertEquals(3, union.size());
        assertTrue(union.contains("p5"));
        assertTrue(union.contains("p1"));
        assertTrue(union.contains("p2"));
        matcher.removePlayerGroup(map2.keySet());
    }

    @Test
    public void testRemoveGroup1() {
        FixedRoomSizeMatcher matcher = new FixedRoomSizeMatcher(3);
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        Map<String, Integer> map3 = new HashMap<>();

        map1.put("p1", 200);
        map1.put("p2", 300);
        map2.put("p3", 20);
        map2.put("p4", 30);
        map3.put("p5", 100);

        assertEquals(null, matcher.addNewPlayerGroup(map1));
        assertEquals(null, matcher.addNewPlayerGroup(map2));
        matcher.removePlayerGroup(map2.keySet());
        matcher.removePlayerGroup(map1.keySet());

        Set<Set<String>> res = matcher.addNewPlayerGroup(map3);
        assertTrue(res == null);
    }


    private Set<String> flatten(Set<Set<String>> set) {
        Set<String> res = new HashSet<>();
        set.forEach(res::addAll);
        return res;
    }

    @Test
    public void testRemoveGroup2() {
        FixedRoomSizeMatcher matcher = new FixedRoomSizeMatcher(4);
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        Map<String, Integer> map3 = new HashMap<>();

        map1.put("p1", 200);
        map1.put("p2", 300);
        map2.put("p3", 20);
        map2.put("p4", 30);
        map3.put("p5", 100);

        assertEquals(null, matcher.addNewPlayerGroup(map1));
        //assertNotEquals(null, matcher.addNewPlayerGroup(map2));
        matcher.removePlayerGroup(map2.keySet());
        matcher.removePlayerGroup(map1.keySet());

        Set<Set<String>> res = matcher.addNewPlayerGroup(map3);
        assertTrue(res == null);
    }


}