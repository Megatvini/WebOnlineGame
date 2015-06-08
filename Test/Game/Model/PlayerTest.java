package Game.Model;

import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.assertFalse;

import Game.Model.Cell;
import Game.Model.Player;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * Created by SHAKO on 08-Jun-15.
 */
public class PlayerTest {

    String name;
    boolean active;
    int potNum;

    Cell startCell;

    Player p;
    Player p1;

    @Before
    public void setUp() throws Exception {
        name = "killera";
        active = true;
        potNum = 4;

        startCell = new Cell(3, 16);

        p = new Player(name, active, potNum);
        p1 = new Player(name, active, potNum, startCell);
    }

    @Test
    public void testGetName() throws Exception {
        assert(name.equals(p.getName()));
    }

    @Test
    public void testGetActive() throws Exception {
        assert(active == p.getActive());
    }

    @Test
    public void testSetActive() throws Exception {
        boolean active;

        active = false;
        p.setActive(active);
        assert(active == p.getActive());

        active = false;
        p.setActive(active);
        assert(active == p.getActive());

        active = true;
        p.setActive(active);
        assert(active == p.getActive());

        active = false;
        p.setActive(active);
        assert(active == p.getActive());
    }

    @Test
    public void testGetPotNum() throws Exception {
        assert(potNum == p.getPotNum());
    }

    @Test
    public void testPotionPlus() throws Exception {
        int potNum = p.getPotNum();

        int addPotNum = 13;
        for (int i = 0; i < addPotNum; i++) {
            p.potionPlus();
        }

        assert(potNum + addPotNum == p.getPotNum());
    }

    @Test
    public void testSetPotNum() throws Exception {
        int potNum;

        potNum = 7;
        p.setPotNum(potNum);
        assert(potNum == p.getPotNum());


        potNum = 71343;
        p.setPotNum(potNum);
        assert(potNum == p.getPotNum());
    }

    @Test
    public void testNegativePotNum() throws Exception {
        // must throw exception if you pass negative number of potions
        // p.setPotNum(-1);
    }

    @Test
    public void testHasStartCell() throws Exception {
        assert p1.hasStartCell();

        // start cell is null
        p1 = new Player(name, active, potNum, null);
        assertFalse(p.hasStartCell());
    }

    @Test
    public void testGetStartCell() throws Exception {
        assert startCell.equals(p1.getStartCell());
    }

    @Test
    public void testSetStartCell() throws  Exception {
        p.setStartCell(startCell);

        assert p.getStartCell().equals(startCell);
    }

    @Test
    public void testGetPosition() throws Exception {
        double x = 43.3;
        double y = 34.23;
        Point2D.Double pos = new Point2D.Double(x, y);
        p.setPosition(pos);

        assert(p.getPosition().equals(pos));
    }

    @Test
    public void testSetPosition() throws Exception {
        double x = 43.3;
        double y = 34.23;

        Point2D.Double pos = new Point2D.Double(x, y);
        p.setPosition(pos);
        assert(p.getPosition().equals(pos));

        p.setPosition(x, y);
        Point2D.Double posit = p.getPosition();
        assert(x == posit.x && y == posit.y);
    }

    @Test //@@ ifiqre(kitxe jgups) jsonis ageba rogor shamowmo an vafshe ageba metodi ro gaq playershi eg kaia ? google-s library-c ganixile gjuftan
    public void testToJsonBuilder() throws Exception {
        //String pJson = "{\"active\":true,\"name\":\"killera\",\"position\":{\"x\":0.0,\"y\":0.0}}";
        System.out.println("Look at Json object and check if has structure u wanted: \n" + p.toJsonBuilder().build());
        //System.out.println(pJson);
        //assert pJson.equals(p.toJsonBuilder().build().toString());
    }
}