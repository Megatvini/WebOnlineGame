package Game.Model;

import java.io.*;
import java.util.Properties;

/**
 * Created by SHAKO on 02-Jun-15.
 */
public class ConfigFile {
/////// I had to write my path :/
    public static final String fileName = "D:\\WebOnlineGame\\src\\ConfigFile.txt";

    public static void loadFromFile(Properties prop, String fileName) {
        InputStream input = null;

        try {

            input = new FileInputStream(fileName);
            // load a properties file
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
     * method must be invoked once in our LIFETIME. it defines file for default key values.
     * even at starting server after shut down its not needed to call this method as long as file is in our hands.
     * only if we want to change default values we can change this method and call it again anywhere just to modify file.
     * anyway we can modify file directly and don't use this method at all, but in that case we have to
     * know format, how to write in file to be readable by Properties object, it's not hard but if u are lazy enough
     * you can just change values we are adding here in this method and re-call once to modify file, even add
     * setProperty methods, we can do anything to this method to get desirable configuration file.
     */
    public static void fill() {
        Properties prop = new Properties();

        /** @@ es shesacvlelia es comentari
         * @param wallWidth width of maze wall, distance between, horizontal or vertical, adjacent corridors
         * @param corridorWidth width of horizontal or vertical corridor
         * @param pm abstract representation of maze, represents some maze and we can check where are and where are not walls
         * @param players names of players
         * @param pRadius radius of circle(s) representing player(s)
         * @param maxMove maximum length in pixels player can move from older location if game is on
         * @param dist minimum distance in pixels player can approach another player without risk of kicking out on of them
         * @param plusDist size in pixels to increase dist variable in every certain amount of time, if game is on
         * @param plusDistDelay length of time period in milliseconds to increase dist with plusDist in. not absolutely real time
         * @param potionNum number of potions to appear before game starts on random places
         * @param potRadius radius of circle(s) representing potion(s)
         * @param addPotDelay length of time period in milliseconds to add new potion at random place in. not absolutely real time
         * @param potForKick number of potions player receives if kicks one of other players
         * @param gameOn represents if is on or not. if true passed game will at the end of constructor.
         */

        prop.setProperty("maxPlayers", "4");
        prop.setProperty("width", "1136");
        prop.setProperty("height", "656");
        prop.setProperty("wallWidth", "16"); // note: does not matter horizontal or vertical
        prop.setProperty("numRows", "14");
        prop.setProperty("numCols", "24");
        prop.setProperty("pRadius", "12"); // make sure player radius is more than half of corridor width
        prop.setProperty("maxMove", "12"); // @@ rezom unda shecvalos es savaraudod
        prop.setProperty("startDist", "100");
        prop.setProperty("plusDist", "4");
        prop.setProperty("plusDistDelay", "1000");
        prop.setProperty("potRadius", "6"); // make sure can fit in corridor
        prop.setProperty("startPotNum", "6");
        prop.setProperty("addPotDelay", "12000");
        prop.setProperty("potForKick", "3");

        //@@ cinc ar unda iyo shen PlaneMazes shemqmnelo numRow=14, numCol=24 qeni da Cell dagijdeba 32 zomis sigrdzec da siganec. mainc lamazi/logikuri iqneba kvadratuli cell, tu ar ginda kiseri gitexia, an tu sxva monacemebs shecvli.

        storeInFile(prop, fileName);

    }

    public static void storeInFile(Properties prop, String fileName) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(fileName);
            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
