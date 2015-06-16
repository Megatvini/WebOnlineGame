package Game.Controller;

import Game.Model.Configuration;
import Game.Model.GameWorld;
import Game.Model.PlaneMaze;
import Game.Model.iWorld;

/**
 * Created by Nika on 21:11, 6/6/2015.
 */

/**
 * creates instances of GameWorld Objects
 */
public class GameFactory {
    public iWorld getNewInstance() {
        Configuration config = Configuration.getInstance();
        PlaneMaze maze = new PlaneMaze(config.getNumRows(), config.getNumCols());
        maze.makePerfect();
        return new GameWorld(maze, config);
    }
}
