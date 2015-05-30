package Game.Model;

import java.util.List;

/**
 * Created by Nika on 16:28, 5/26/2015.
 */
public interface iWorld {

    /**
     * adds new player named playerName to the world
     * @param playerName name of a new player
     * @return true if player was Successfully added
     * returns false if there already is a player with playerName
     * or if there already are maximum number of players
     */
    boolean addPlayer(String playerName);

    /**
     *
     */
    void startGame();

    /**
     * size is measured in pixels
     * @return height of world
     */
    double getHeight();

    /**
     * size is measured in pixels
     * @return width of a world
     */
    double getWidth();

    /**
     * when world class is created it should create
     * random maze in itself
     * @return string interpretation of a maze
     *
     */
    PlaneMaze getMaze();

    /**
     * maze is a grid, in which each cell represents constant
     * number of world pixels;
     * @return width of the maze inside the world
     */
    int getMazeWidth();


    /* maze is a grid, in which each cell represents constant
     * number of world pixels;
     * @return height of a maze inside the world
     */
    int getMazeHeight();

    /**
     * this does not include maze
     * @return state of the world
     * it returns coordinates of players and potions
     */
    Object[] getState();

    /**
     *
     * @return how many players are currently in world
     */
    int numberOfPlayers();


    /**
     * tries to move player in (dx, dy) direction
     * @param PlayerName name of the player which tries to move
     * @param dx x component of direction vector
     * @param dy y component of direction vector
     * @return true iff player moved
     * player can not move, if maze wall is blocking its path
     */
    boolean playerMove(String PlayerName, double dx, double dy);

    /**
     * tries to put player on (x, y) coordinates
     * @param playerName
     * @param x cartesian coordinate
     * @param y cartesian coordinate
     * @return true iff player was able to move
     * from its current position to (x,y) on linear path
     * false, if maze wall blocked its;
     */
    boolean setPlayerCoordinates(String playerName, double x, double y);


    /**
     *
     * @return list of all player names currently in world
     */
    List<String> getPlayers();
//@@ get wallW corW
    boolean gameOn();
}
