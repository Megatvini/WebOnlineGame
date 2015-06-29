package Core.Controller;

import Core.Bean.Account;
import Core.Dao.AccountDao;
import Core.Dao.GameDao;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Created by Nika on 00:55, 6/29/2015.
 */
public class GameManagerTest {
    @Test
    public void testAddNewGameResults() throws Exception {
        AccountDao accountDaoMock = mock(AccountDao.class);
        GameDao gameDaoMock = mock(GameDao.class);

        GameManager gameManager = new GameManager(accountDaoMock, gameDaoMock);
        List<String> gameResult = new ArrayList<>();
        gameResult.add("nika");
        gameResult.add("rezo");
        gameResult.add("shako");
        gameResult.add("guka");
        when(accountDaoMock.getUser(any())).thenReturn(new Account());
        gameManager.addNewGameResults(gameResult);
        for (String s : gameResult) {
            verify(accountDaoMock).getUser(s);
        }
        verify(accountDaoMock, times(4)).changeUser(any());
        verify(gameDaoMock).addNewGame(any());


    }
}