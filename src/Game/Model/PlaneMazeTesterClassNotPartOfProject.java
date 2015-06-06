package Game.Model;

import javax.json.Json;
import javax.json.JsonWriter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class PlaneMazeTesterClassNotPartOfProject extends JApplet {

    private PlaneMaze pm;

    public PlaneMazeTesterClassNotPartOfProject(PlaneMaze pm) {
        this.pm = pm;
        setBackground(Color.white);
        setForeground(Color.white);
    }


    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(Color.black);

        int x = 5;
        int y = 7;

        //g2.draw(new Line2D.Double(x, y, 200, 200));

        ///////////////draw maze ///////////////////////////////

        double startX = 5;
        double startY = 5;

        double wallS = 15;

        //////draw orders
        g2.setPaint(Color.red);
        for (int i = 0; i < pm.numCols(); i++) {
            g2.draw(new Line2D.Double(startX + i * wallS,
                    startY,
                    startX + (i + 1) * wallS,
                    startY));
            g2.draw(new Line2D.Double(startX + i * wallS,
                    startY + pm.numRows() * wallS,
                    startX + (i + 1) * wallS,
                    startY + pm.numRows() * wallS));
        }
        for (int i = 0; i < pm.numRows(); i++) {
            g2.draw(new Line2D.Double(startX,
                    startY + i * wallS,
                    startX,
                    startY + (i + 1) * wallS));
            g2.draw(new Line2D.Double(startX + pm.numCols() * wallS,
                    startY + i * wallS,
                    startX + pm.numCols() * wallS,
                    startY + (i + 1) * wallS));

        }
        g2.setPaint(Color.black);
        //////END borders

        for (int i = 0; i < pm.numRows(); i++) {

            for (int j = 0; j < pm.numCols(); j++) {

                if (j < pm.numCols() - 1) {
                    if (pm.isWall(i, j, i, j + 1)) {
                        g2.draw(new Line2D.Double(startX + (j + 1) * wallS,
                                startY + i * wallS,
                                startX + (j + 1) * wallS,
                                startY + (i + 1) * wallS));
                    }
                }

                if (i < pm.numRows() - 1) {
                    if (pm.isWall(i, j, i + 1, j)) {
                        g2.draw(new Line2D.Double(startX + j * wallS,
                                startY + (i + 1) * wallS,
                                startX + (j + 1) * wallS,
                                startY + (i + 1) * wallS));
                    }
                }
            }
        }


        ///////////////END draw maze ///////////////////////////
    }


    private static Collection<String> testMethod(ArrayList<String> arr, List<String> l) {
        return new ArrayList<String>();
    }

    public static void main(String s[]) {

        //testMethod(new ArrayList<>(), new ArrayList<>());

//        GameWorld gw = new GameWorld(new ArrayList<String>(Arrays.asList("shako", "killera", "selapa")), new PlaneMaze(14, 24), true);
//        gw.finishGame();

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }



//        try(JsonWriter jsonWriter = Json.createWriter(System.out)) {
//            jsonWriter.write(gw.getState());
//        }
        //System.out.println(System.out);


        // properties testing >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//        String value;
//
//        Properties prop = new Properties();
//        InputStream input = null;
//
//        try {
//
//            input = new FileInputStream(ConfigFile.fileName);
//
//            // load a properties file
//            prop.load(input);
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } finally {
//            if (input != null) {
//                try {
//                    input.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        // get the property value and initialize public static final variables of this class
//        value = prop.getProperty("key1");
//
//        System.out.println(value);

        //Plane Maze Testing shit >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        JFrame f = new JFrame("ShapesDemo2D");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        PlaneMaze pm = new PlaneMaze(14, 24);
        pm.makePerfect();
        //pm.makeThiner(0.4);
        //pm.draw();

//        GameWorld gw = new GameWorld(pm);
//
//        gw.addPlayer("shako");
//        gw.addPlayer("nika");
//        gw.addPlayer("killera");
//
//        gw.startGame();
//
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(gw.getUpdate("shako"));
//                System.out.println(gw.getInit());
//
//            }
//        });
//
//        t.start();


        JApplet applet = new PlaneMazeTesterClassNotPartOfProject(pm);
        f.getContentPane().add("Center", applet);
        applet.init();

        f.pack();
        f.setSize(new Dimension(600, 800));
        f.show();

        //System.out.println(pm.toString());

        //{"gameOn":false, "potNum":0, "players":[{"active":true, "name":"killera", "position":{"x":4.0, "y":4.0}}, {shemdegi motamashe}, ...], "potions":[{"x":922.7532565354794,"y":157.67468138268043},{"x":19.089561546649072,"y":156.78505392060384}, {shemdegi potion}, ...],"distance":100.0}

    }


}







































