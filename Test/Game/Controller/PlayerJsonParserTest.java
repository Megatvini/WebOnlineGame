package Game.Controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Nika on 17:47, 6/7/2015.
 */
public class PlayerJsonParserTest {
    private String initMessage =
            "{ \"type\":\"init\",\n" +
            "\"name\":\"rezo\"\n" +
            "}";
    private String updateMessage =
            "{" +
            "        \"type\":\"update\",\n" +
            "        \"name\":\"rezo\",\n" +
            "        \"coordinates\": {\n" +
            "                \"x\": 10,\n" +
            "                \"y\": 20\n" +
            "        }\n" +
            "}";


    private PlayerJsonParser initParser;
    private PlayerJsonParser updateParser;


    @Before
    public void setUp() {
        initParser = new PlayerJsonParser(initMessage);
        updateParser = new PlayerJsonParser(updateMessage);
    }

    @Test
    public void testGetCommand() throws Exception {
        assertEquals("init", initParser.getCommand());
        assertEquals("update", updateParser.getCommand());
    }

    @Test
    public void testGetPlayerName() throws Exception {
        assertEquals("rezo", initParser.getPlayerName());
        assertEquals("rezo", updateParser.getPlayerName());
    }

    @Test
    public void testGetXCoord() throws Exception {
        assertEquals(0, initParser.getXCoord());
        assertEquals(10, updateParser.getXCoord());
    }

    @Test
    public void testGetYCoord() throws Exception {
        assertEquals(0, initParser.getYCoord());
        assertEquals(20, updateParser.getYCoord());
    }
}