package MatchMaking;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by Nika on 23:14, 6/11/2015.
 */
public class StartingGroupTest {

    @Test
    public void testStartingGroup() throws Exception {
        StartingGroup group = new StartingGroup("nika");
        group.addUser("vigac1");
        group.addUser("vigac2");
        Collection<String> col = group.getGroup();
        assertEquals(3, col.size());
        assertTrue(col.contains("nika"));
        assertTrue(col.contains("vigac1"));
        assertTrue(col.contains("vigac2"));
    }

    @Test
    public void testGetCreator() {
        StartingGroup group = new StartingGroup("nika");
        assertTrue(group.getCreator().equals("nika"));
    }
}