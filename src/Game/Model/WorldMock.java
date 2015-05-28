package Game.Model;

import java.util.List;

/**
 * Created by Nika on 00:49, 5/29/2015.
 */
public class WorldMock implements World {
    /**
     * size is measured in pixels
     *
     * @return height of world
     */
    @Override
    public double getHeight() {
        return 0;
    }

    /**
     * size is measured in pixels
     *
     * @return width of a world
     */
    @Override
    public double getWidth() {
        return 0;
    }

    /**
     * when world class is created it should create
     * random maze in itself
     *
     * @return string interpretation of a maze
     */
    @Override
    public String getMaze() {
        return null;
    }

    /**
     * maze is a grid, in which each cell represents constant
     * number of world pixels;
     *
     * @return width of the maze inside the world
     */
    @Override
    public int getMazeWidth() {
        return 0;
    }

    @Override
    public int getMazeHeight() {
        return 0;
    }

    /**
     * this does not include maze
     *
     * @return state of the world
     * it returns coordinates of players and potions
     */
    @Override
    public String getState() {
        return null;
    }

    /**
     * adds new player named playerName to the world
     *
     * @param playerName name of a new player
     * @return true if player was Successfully added
     * returns false if there already is a player with playerName
     * or if there already are maximum number of players
     */
    @Override
    public boolean addPlayer(String playerName) {
        return false;
    }

    /**
     * @return how many players are currently in world
     */
    @Override
    public int numberOfPlayers() {
        return 0;
    }

    /**
     * tries to move player in (dx, dy) direction
     *
     * @param PlayerName name of the player which tries to move
     * @param dx         x component of direction vector
     * @param dy         y component of direction vector
     * @return true iff player moved
     * player can not move, if maze wall is blocking its path
     */
    @Override
    public boolean playerMove(String PlayerName, double dx, double dy) {
        return false;
    }

    /**
     * tries to put player on (x, y) coordinates
     *
     * @param playerName
     * @param x          cartesian coordinate
     * @param y          cartesian coordinate
     * @return true iff player was able to move
     * from its current position to (x,y) on linear path
     * false, if maze wall blocked its;
     */
    @Override
    public boolean setPlayerCoordinates(String playerName, double x, double y) {
        return false;
    }

    /**
     * @return list of all player names currently in world
     */
    @Override
    public List<String> getPlayers() {
        return null;
    }
}
