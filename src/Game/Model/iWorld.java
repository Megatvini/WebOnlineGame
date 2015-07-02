package Game.Model;

import javax.json.JsonObject;
import java.util.Collection;
import java.util.List;

/**
 * Created by Nika on 16:28, 5/26/2015.
 */
public interface iWorld {

    /**
     * Tries to add new player with given name at one of corner, on some random part of cell.
     * First tries up-left corner, then tries next clockwise. If player conflicts with
     * one of players(i.e. it is near to one of player then determined distance and those two
     * have different count of potions) or potion(i.e. it overlaps one of potions), tries
     * next corner, if does not finds proper corner does not adds new player. If already
     * added max count of players, or if already have player with that name, false returned.
     * By default: new players active state is true, potion number is zero.
     * @param playerName name of new player to add
     * @return true iff player added
     */
    boolean addPlayerAtCorner(String playerName);

    /**
     * @return abstract representation of maze, represents some maze and we can check where are and where are not walls
     */
    JsonObject getInit();

    /**
     * this does not include maze
     * @return state of the world
     * it returns coordinates of players and potions
     */
    JsonObject getUpdate(String playerName);

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
    Collection<String> getPlayers();


    /**
     * returns collection in which are kicked players, player which
     * lose earlier has lower index
     * @return collection of kicked players, player which lose earlier has lower index
     */
    List<String> playerPlaces();

    /**
     * @return game state on or not.
     */
    boolean isFinished();

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
    void finishGame();
}
