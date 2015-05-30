package Game.Model;

import javafx.util.Pair;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * Created by SHAKO on 30-May-15.
 */
public class World implements iWorld {

    public static int MAX_PLAYERS = 4;

    private double wallWidth;
    private double corridorWidth;
    private PlaneMaze pm;
    private double pWidth;
    private double pHeight;
    private ArrayList<String> playerNames;
    private double dist;
    private double plusDist;
    private double plusDistDelay;
    private ArrayList<Point2D.Double> potions;
    private double addPotDelay;
    private boolean gameOn;

    private double width;
    private double height;
    private Map<String, Player> nameOnPlayer;
    private Map<String, Point2D.Double> nameOnPosit;


    public World(double wallWidth, double corridorWidth, PlaneMaze pm, double pWidth, double pHeight, ArrayList<String> players, double dist, double plusDist, double plusDistDelay, ArrayList<Point2D.Double> potions, double addPotDelay, boolean gameOn) {
            this.wallWidth = wallWidth;
            this.corridorWidth = corridorWidth;
            this.pm = pm;

            this.width = pm.numCols() * corridorWidth + (pm.numCols() - 1) * wallWidth;
            this.height = pm.numRows() * corridorWidth + (pm.numRows() - 1) * wallWidth;

            this.pWidth = pWidth;
            this.pHeight = pHeight;
            playerNames = players;

            nameOnPlayer = new HashMap<String, Player>();
            nameOnPosit = new HashMap<String, Point2D.Double>();

            for (int i = 0; i < playerNames.size(); i++) {
                addPlayer(playerNames.get(i));
            }

            this.dist = dist;
            this.plusDist = plusDist;
            this.plusDistDelay = plusDistDelay;
            this.potions = potions; // @@ potions da aseve yvela array saxelebi da ase shmdeg tu kide ramea copy clone shit...
            this.addPotDelay = addPotDelay;
            this.gameOn = gameOn;

            if (gameOn) {
                startGame();
            }
    }

    @Override
    public boolean addPlayer(String playerName) {
        if (!gameOn || playerNames.size() > MAX_PLAYERS || playerNames.contains(playerName)) {
            Player p = null;
            switch (playerNames.size()) {
                case 0:
                    p = new Player((corridorWidth - pWidth) / 2, (corridorWidth - pHeight) / 2, pWidth, pHeight);
                    break;
                case 1:
                    p = new Player((pm.numCols() - 1) * corridorWidth + (pm.numCols() - 1) * wallWidth + (corridorWidth - pWidth) / 2,
                            (corridorWidth - pHeight) / 2,
                            pWidth,
                            pHeight);
                    break;
                case 2:
                    p = new Player((pm.numCols() - 1) * corridorWidth + (pm.numCols() - 1) * wallWidth + (corridorWidth - pWidth) / 2,
                            (pm.numRows() - 1) * corridorWidth + (pm.numRows() - 1) * wallWidth + (corridorWidth - pHeight) / 2,
                            pWidth,
                            pHeight);
                    break;
                case 3:
                    p = new Player((corridorWidth - pWidth) / 2,
                            (pm.numRows() - 1) * corridorWidth + (pm.numRows() - 1) * wallWidth + (corridorWidth - pHeight) / 2,
                            pWidth,
                            pHeight);
                    break;
                default:
                    return false;
            }
            playerNames.add(playerName);
            nameOnPlayer.put(playerName, p);
            nameOnPosit.put(playerName, p.getPosition());
            return true;
        }
        return false;
    }

    @Override
    public void startGame() {

    }



    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }


    @Override
    public PlaneMaze getMaze() {
        return pm;
    }

    @Override
    public int getMazeWidth() {
        return pm.numCols();
    }

    @Override
    public int getMazeHeight() {
        return pm.numRows();
    }

    @Override //@@ xom ar joiba misaarts rom aigebs imis stringad gadaqcevisas lockaves rameebs(safiqria aq ras)
    public Object[] getState() { //@@ misamartis copy ar unda davubruno savaraudod, elementis copy minda, mara elem-copy didi operaciaa duuh
        return new Object[]{nameOnPosit, potions};
    }



    @Override
    public int numberOfPlayers() {
        return playerNames.size();
    }

    @Override
    public boolean playerMove(String PlayerName, double dx, double dy) {
        return false;
    }

    @Override
    public boolean setPlayerCoordinates(String playerName, double x, double y) {

        return false;
    }

    @Override
    public List<String> getPlayers() {
        return playerNames;
    }

    @Override
    public boolean gameOn() {
        //double[] h = new Player(1, 2).getPosition();
        return false;
    }



    private class Player {

        private  double x;
        private double y;

        private double width;
        private double height;

        private Player(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;

            this.width = width;
            this.height = height;
        }

        private Point2D.Double getPosition() {
            return new Point2D.Double(x, y);
        }

    }

}
