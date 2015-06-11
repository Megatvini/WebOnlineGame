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
    Player.StartCellT sct;
    Cell startCell;
    Player p;

    @Before
    public void setUp() throws Exception {
        name = "killera";
        active = true;
        potNum = 4;
        sct = Player.StartCellT.given;
        startCell = new Cell(3, 16);

        p = new Player(name, active, potNum, startCell);
    }

    @Test
    public void testMultipeConstructors() {
        Player p1 = new Player(name);

        Player p2 = new Player(name, active, potNum, Player.StartCellT.atCorner);
        Player p3 = new Player(name, active, potNum, Player.StartCellT.given);
        Player p4 = new Player(name, active, potNum, Player.StartCellT.random);

        // p1: checking defaults
        assert p1.getName() == name;
        assert p1.getActive() == true;
        assert p1.getPotNum() == 0;
        assert p1.getStartCellT() == Player.StartCellT.atCorner;

        // p2: checking pllayer with start place type "atCorner"
        assert p2.getName() == name;
        assert p2.getActive() == active;
        assert p2.getPotNum() == potNum;
        assert p2.getStartCellT() == Player.StartCellT.atCorner;

        // p3: checking pllayer with start place type "given"
        assert p3.getName() == name;
        assert p3.getActive() == active;
        assert p3.getPotNum() == potNum;
        assert p3.getStartCellT() == Player.StartCellT.given;
        assert p3.getStartCell().equals(Player.defStartCell); // compare if its default

        // p4: checking pllayer with start place type "random"
        assert p4.getName() == name;
        assert p4.getActive() == active;
        assert p4.getPotNum() == potNum;
        assert p4.getStartCellT() == Player.StartCellT.random;
    }

    @Test
    public void testGetName() throws Exception {
        assert(name.equals(p.getName()));
    }

    @Test
    public void testGetActive() throws Exception {
        assert(true == p.getActive());
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
        assert(p.getPotNum() == potNum);
    }

    @Test
    public void testPotionPlus() throws Exception {
        int potNum = p.getPotNum();

        int addPotNum = 13;
        for (int i = 0; i < addPotNum; i++) {
            p.potionPlus();
        }

        assert(p.getPotNum() == potNum + addPotNum);
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
    public void testGetStartCellT() throws Exception {
        assert p.getStartCellT() == sct;
    }

    @Test
    public void testSetStartCellT() throws Exception {
        Player.StartCellT sct;

        sct = Player.StartCellT.atCorner;
        p.setStartCellT(sct);
        assert p.getStartCellT() == sct;

        sct = Player.StartCellT.given;
        p.setStartCellT(Player.StartCellT.given);
        assert p.getStartCellT() == sct;


        sct = Player.StartCellT.random;
        p.setStartCellT(Player.StartCellT.random);
        assert p.getStartCellT() == sct;
    }

    @Test
    public void testGetStartCell() throws Exception {
        assert p.getStartCell().equals(startCell);

        p = new Player(name, active, potNum, Player.StartCellT.given);
        assert p.getStartCell().equals(Player.defStartCell);
        p.setStartCell(startCell);
        assert p.getStartCell().equals(startCell);

    }

    @Test
    public void testSetStartCell() throws  Exception {
        Cell c = new Cell(1, 12);
        p.setStartCell(c);
        assert p.getStartCell().equals(c);
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

    @Test
    public void testEquals() throws Exception {
        assert p.equals(new Player(name));
    }

    @Test //@@ ifiqre(kitxe jgups) jsonis ageba rogor shamowmo an vafshe ageba metodi ro gaq playershi eg kaia ? google-s library-c ganixile gjuftan
    public void testToJsonBuilder() throws Exception {
        //String pJson = "{\"active\":true,\"name\":\"killera\",\"position\":{\"x\":0.0,\"y\":0.0}}";
        System.out.println("Look at Json object and check if has structure u wanted: \n" + p.toJsonBuilder().build());
        //System.out.println(pJson);
        //assert pJson.equals(p.toJsonBuilder().build().toString());
    }
}