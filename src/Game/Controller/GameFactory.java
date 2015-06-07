package Game.Controller;

import Game.Model.GameWorld;
import Game.Model.PlaneMaze;
import Game.Model.iWorld;

/**
 * Created by Nika on 21:11, 6/6/2015.
 */
public class GameFactory {
    public iWorld getNewInstance() {
        PlaneMaze maze = new PlaneMaze(20, 20);
        maze.makePerfect();
        return new GameWorld(maze);
    }
}
