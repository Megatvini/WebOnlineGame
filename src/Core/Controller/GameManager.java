package Core.Controller;

import Core.Bean.Game;
import Core.Dao.AccountDao;
import Core.Dao.GameDao;
import Interfaces.iAccount;

import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nika on 16:38, 6/26/2015.
 */

public class GameManager {
    private AccountDao accountDao;
    private GameDao gameDao;

    public GameManager(AccountDao accountDao, GameDao gameDao) {
        this.accountDao = accountDao;
        this.gameDao = gameDao;
    }

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

    private List<Integer> calculateRatingChanges(List<String> playerPositions) {
        List<Integer> result = new ArrayList<>();
        for (int i=0; i<playerPositions.size(); i++) {
            result.add(0);
        }
        return result;
    }
}
