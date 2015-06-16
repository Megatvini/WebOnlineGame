package Game.Model;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by SHAKO on 15-Jun-15.
 */
public class Configuration {

    // file name to read configuration info from
    private static final String fileName = "ConfigFile.properties";

    // Properties object to read variables from
    private Properties prop;

    // final variables whose values read from configuration file
    private int maxPlayers;
    private int numRows;
    private int numCols;
    private double width;
    private double height;
    private double wallWidth;
    private double pRadius;
    private double maxMove;
    private double startDist;
    private double plusDist;
    private long plusDistDelay;
    private double potRadius;
    private int startPotNum;
    private long addPotDelay;
    private int potForKick;

    // have to calculate depended on configuration info
    private double cellWidth;
    private double cellHeight;

    private static Configuration instance = null;

    /**
     * constructor for only testing purposes
     */
    public Configuration(String fileName) {
        prop = new Properties();
        loadFromFile(prop, fileName);

        readVariables();

        try {
            checkConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Configuration() {
        // object to write info from file into, and read from it then
        prop = new Properties();
        loadFromFile(prop, fileName);

        readVariables();

        try {
            checkConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * writes info from file, with given name, into given properties object
     * @param prop write info from specified file into this
     * @param fileName searches file with this name to load info from into given properties object
     */
    private static void loadFromFile(Properties prop, String fileName) {
        InputStream input = null;
        try {
            input = GameWorld.class.getClassLoader().getResourceAsStream(fileName);
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * reading info into  variables from properties object
     */
    private void readVariables() {
        maxPlayers = Integer.parseInt(prop.getProperty("maxPlayers"));
        numRows = Integer.parseInt(prop.getProperty("numRows"));
        numCols = Integer.parseInt(prop.getProperty("numCols"));
        width = Double.parseDouble(prop.getProperty("width"));
        height = Double.parseDouble(prop.getProperty("height"));
        wallWidth = Double.parseDouble(prop.getProperty("wallWidth"));
        pRadius = Double.parseDouble(prop.getProperty("pRadius"));
        maxMove = Double.parseDouble(prop.getProperty("maxMove"));
        startDist = Double.parseDouble(prop.getProperty("startDist"));
        plusDist = Double.parseDouble(prop.getProperty("plusDist"));
        plusDistDelay = Long.parseLong(prop.getProperty("plusDistDelay"));
        potRadius = Double.parseDouble(prop.getProperty("potRadius"));
        startPotNum = Integer.parseInt(prop.getProperty("startPotNum"));
        addPotDelay = Long.parseLong(prop.getProperty("addPotDelay"));
        potForKick = Integer.parseInt(prop.getProperty("potForKick"));

        // calculate some values depended on value read from file
        cellWidth = ((width - (numCols - 1) * wallWidth)) / numCols;
        cellHeight = ((height - (numRows - 1) * wallWidth)) / numRows;
    }

    /**
     * checks if configuration file is well write, for example
     * that double player darius is not more than corridor width
     * or height. If finds some mistate throws proper runtime exception.
     */
    private void checkConfig() {
        if (2 * pRadius > cellWidth || 2 * pRadius > cellHeight) {
            throw new RuntimeException("Player radius is too large for corridor");
        }

        if (2 * potRadius > cellWidth || 2 * potRadius > cellHeight) {
            throw new RuntimeException("Potion radius is too large for corridor");
        }
    }

    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getWallWidth() {
        return wallWidth;
    }

    public double getPRadius() {
        return pRadius;
    }

    public double getMaxMove() {
        return maxMove;
    }

    public double getStartDist() {
        return startDist;
    }

    public double getPlusDist() {
        return plusDist;
    }

    public long getPlusDistDelay() {
        return plusDistDelay;
    }

    public double getPotRadius() {
        return potRadius;
    }

    public int getStartPotNum() {
        return startPotNum;
    }

    public long getAddPotDelay() {
        return addPotDelay;
    }

    public int getPotForKick() {
        return potForKick;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public double getCellHeight() {
        return cellHeight;
    }
}
