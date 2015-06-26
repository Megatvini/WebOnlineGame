package Core.Controller;

import Core.Dao.AccountDao;
import Core.Dao.GameDao;
import Interfaces.iAccount;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nika on 16:38, 6/26/2015.
 */

public class GameManager {
    public static final int DEFAULT_RATING = 1200;
    private AccountDao accountDao;
    private GameDao gameDao;

    public GameManager(AccountDao accountDao, GameDao gameDao) {
        this.accountDao = accountDao;
        this.gameDao = gameDao;
    }

    /**
     * takes game result, saves it in database and
     * changes player rating according to results
     * @param playerPositions game result, positions of players
     *                        //winner is on last index
     */
    public void addNewGameResults(List<String> playerPositions) {
        //for each player count their ratingChange and put participation
        //give each account new rating and save it to database;

        int gameID = gameDao.addNewGame(new Date(System.currentTimeMillis()));
        List<Integer> ratingChanges = calculateRatingChanges(playerPositions);

        for (int i=0; i < playerPositions.size(); i++) {
            try {
                iAccount account = accountDao.getUser(playerPositions.get(i));
                int ratingChange = ratingChanges.get(i);
                gameDao.addParticipation(gameID, account.getID(), ratingChange);
                account.setRating(account.getRating() + ratingChange);
                accountDao.changeUser(account);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * takes current player positions and
     * calculates their ratingChanges
     * @param playerPositions positions of player after game
     * @return rating changes accordingly
     */
    private List<Integer> calculateRatingChanges(List<String> playerPositions) {
        List<Integer> result = new ArrayList<>();
        for (int i=0; i<playerPositions.size(); i++) {
            result.add(0);
        }
        return result;
    }
}
