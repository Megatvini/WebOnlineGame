package Game.Controller;


import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.StringReader;

/**
 * Created by Nika on 22:58, 6/6/2015.
 */

public class PlayerJsonParser {
    private String textToParse;
    private String cmd;
    private String playerName;
    private int xCoord;
    private int yCoord;

    public PlayerJsonParser(String textToParse) {
        this.textToParse = textToParse;
        init();
    }

    /**
     * parse string passed in constructor and save
     * information in private variables
     */
    private void init() {
        JsonParser parser = Json.createParser(new StringReader(textToParse));

        parser.next();
        parser.next();
        parser.next();
        cmd = parser.getString();

        parser.next();
        parser.next();
        playerName = parser.getString();

        if (cmd.equals("init")) return;

        parser.next();
        parser.next();
        parser.next();
        parser.next();
        xCoord =  parser.getInt();

        parser.next();
        parser.next();
        yCoord = parser.getInt();
    }

    public String getCommand() {
        return cmd;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

}
