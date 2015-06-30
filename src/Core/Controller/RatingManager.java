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

public class RatingManager {
    public static final int DEFAULT_RATING = 1200;
    public static final int K_RATING_VALUE = 15;
    private AccountDao accountDao;
    private GameDao gameDao;

    public RatingManager(AccountDao accountDao, GameDao gameDao) {
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
        List<iAccount> accountPositions = getAccountPositions(playerPositions);
        List<Integer> ratingChanges = calculateRatingChanges(accountPositions);
        for (int i=0; i < accountPositions.size(); i++) {
            iAccount account = accountPositions.get(i);
            int ratingChange = ratingChanges.get(i);
            gameDao.addParticipation(gameID, account.getID(), ratingChange);
            account.setRating(account.getRating() + ratingChange);
            accountDao.changeUser(account);
        }
    }

    private List<iAccount> getAccountPositions(List<String> playerPositions) {
        List<iAccount> accounts = new ArrayList<>();
        for (String playerPosition : playerPositions) {
            try {
                accounts.add(accountDao.getUser(playerPosition));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return accounts;
    }


    /**
     * takes current player positions and
     * calculates their ratingChanges
     * @param playerPositions positions of player after game
     * @return rating changes accordingly
     */
    private List<Integer> calculateRatingChanges(List<iAccount> playerPositions) {
        List<Integer> result = new ArrayList<>();
        for (int i=0; i<playerPositions.size(); i++) {
            result.add(calculateRatingChange(playerPositions, i));
        }
        return result;
    }

    private Integer calculateRatingChange(List<iAccount> playerPositions, int index) {
        double change = 0;
        for (int i=0; i<playerPositions.size(); i++) {
            if (i==index) continue;
            change += calculateChange(playerPositions.get(index), playerPositions.get(i), index < i);
        }
        return (int) change;
    }

    private double calculateChange(iAccount player, iAccount opponent, boolean won) {
        int Sa = won?1:0;
        double power = (opponent.getRating() - player.getRating())/400;
        double Ea = 1/(1 + Math.pow(10, power));
        return K_RATING_VALUE*(Sa-Ea);
    }

}
