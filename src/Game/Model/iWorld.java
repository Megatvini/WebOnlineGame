package Game.Model;

import javax.json.JsonObject;
import java.util.List;

/**
 * Created by Nika on 16:28, 5/26/2015.
 */
public interface iWorld {

    /**
     * adds new player named playerName to the world, depends on player index chooses
     * position, distributes at corners, if game is not on and max players have not reached
     * and there is no player in game with same name, otherwise does nothing
     * @param playerName name of a new player
     * @return true if player was Successfully added
     * returns false if game is not on or if there already is a player with playerName
     * or if there already are maximum number of players
     */
    boolean addPlayer(String playerName);

    /**
     * size is measured in pixels
     * @return width of a world
     */
    double getWidth();

    /**
     * size is measured in pixels
     * @return height of world
     */
    double getHeight();

    /**
     * maze is a grid, in which each cell represents constant
     * number of world pixels;
     * @return width of the maze inside the world
     */
    int numCols();


    /**
     *  maze is a grid, in which each cell represents constant
     * number of world pixels;
     * @return height of a maze inside the world
     */
    int numRows();

    /**
     * @return abstract representation of maze, represents some maze and we can check where are and where are not walls
     */
    JsonObject getMaze();

    /**
     * this does not include maze
     * @return state of the world
     * it returns coordinates of players and potions
     */
    JsonObject getState();

    /**
     * @return how many players are currently in world
     */
    int numberOfPlayers();


    /**
     * tries to move player in (dx, dy) direction, player cannot move if it collide with
     * walls or if it lays out of bounds, old position will remain. If game is on player
     * cannot move with more than certain length, old position will remain.
     * @param playerName name of the player which tries to move
     * @param dx x component of direction vector
     * @param dy y component of direction vector
     * @return true iff player moved
     * player can not move, if maze wall is blocking its path or if game is on
     * and move length is more than acceptable
     */
    boolean playerMove(String playerName, double dx, double dy);

    /**
     * tries to put player on (x, y) coordinates, player cannot move if it collide with
     * walls or if it lays out of bounds, old position will remain. If game is on player
     * cannot move with more than certain length, old position will remain.
     * @param playerName
     * @param x cartesian coordinate
     * @param y cartesian coordinate
     * @return true iff player was able to move
     * from its current position to (x,y) on linear path
     * false, if maze wall blocked its; false if wall or bounds blocked or game was on
     * and distance from old to new position is more than acceptable.
     */
    boolean setPlayerCoordinates(String playerName, double x, double y);


    /**
     * @return list of all player names currently in world
     */
    List<String> getPlayers();

    /**
     * @return game state on or not.
     */
    boolean gameOn();

    /**
     * game is on after calling this function, if already is on nothing happens,
     * minimum distance, player can approach other without risk of kicking one of them,
     * will start increase, potions will be added on random positions periodically.
     */
    void startGame();

    /**
     * game is off after calling this function, if already is off nothing happens,
     * potions stop to add, minimum distance, player can approach other
     * without risk of kicking one of them, will stop increase
     */
    void finishGame(); //@@ aq safiqralia kide
}
