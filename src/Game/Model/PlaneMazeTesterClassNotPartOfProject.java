package Game.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

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


    public static void main(String s[]) {

        try {
            throw new Exception("olaa");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("awdawdawd");


//        ArrayList<Point> arr = new ArrayList<Point>();
//
//        arr.add(new Point(1, 2));
//
//        ArrayList<Point> ar = (ArrayList<Point>) arr.clone();
//
//        ar.get(0).setLocation(3, 4);
//
//        System.out.println(arr.get(0));
//        System.out.println(ar.get(0));


        //public World(double wallWidth, double corridorWidth, int numRows, int numCols, ArrayList<String> players, double pWidth, double pHeight, double maxMove, double dist, double plusDist, double plusDistDelay, ArrayList<Point2D.Double> potions, double addPotDelay, boolean gameOn) {

        //World w = new World(1, 1, 1, 1, new ArrayList<String>(), 1, 1, 1, 1, 1, 1, new ArrayList<Point2D.Double>(), 1, false);
        //w.makePerfect();


        //Plane Maze Testing shit
//        JFrame f = new JFrame("ShapesDemo2D");
//        f.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                System.exit(0);
//            }
//        });
//
//        for (int i = 0; i < 5; i++) {
//
//            PlaneMaze pm = new PlaneMaze(35, 36);
//            pm.makePerfect();
//            //pm.makeThiner(0.4);
//            pm.draw();
//            JApplet applet = new PlaneMazeTesterClassNotPartOfProject(pm);
//            f.getContentPane().add("Center", applet);
//            applet.init();
//
//            f.pack();
//            f.setSize(new Dimension(600, 800));
//            f.show();
//
//        }

    }


}







































