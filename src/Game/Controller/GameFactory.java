package Game.Controller;

import Game.Model.*;

/**
 * Created by Nika on 21:11, 6/6/2015.
 */

/**
 * creates instances of GameWorld Objects
 */
public class GameFactory {
    public iWorld getNewInstance() {
        Configuration config = Configuration.getInstance();
        GameMaze maze = new GameMaze(config);
        maze.makePerfect();
        maze.makeThinner(0.3);
        return new GameWorld(maze, config);
    }
}
