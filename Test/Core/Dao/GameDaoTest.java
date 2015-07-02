package Core.Dao;

import Core.Bean.Game;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Nika on 21:15, 6/28/2015.
 */
public class GameDaoTest {
    private DataSource dataSourceMock;
    private Connection connectionMock;
    private PreparedStatement preparedStatementMock;
    private ResultSet resultSetMock;

    private void initMocks() throws SQLException {
        dataSourceMock = mock(DataSource.class);
        connectionMock = mock(Connection.class);
        preparedStatementMock = mock(PreparedStatement.class);
        resultSetMock = mock(ResultSet.class);
        when(dataSourceMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    }

    @Test
    public void testAddNewGame() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getInt(anyInt())).thenReturn(5);
        Date date = new Date(0);
        int n = gameDao.addNewGame(date);
        assertEquals(5, n);
        verify(preparedStatementMock).setTimestamp(anyInt(), anyObject());
        verify(connectionMock).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testAddNewGameConnException() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        assertEquals(0, gameDao.addNewGame(new Date()));
    }

    @Test
    public void testAddNewGameSTException() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(0, gameDao.addNewGame(new Date()));
        verify(connectionMock).close();
    }

    @Test
    public void testAddNewGameSTException1() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString()))
                .thenReturn(preparedStatementMock)
                .thenThrow(new SQLException());
        assertEquals(0, gameDao.addNewGame(new Date()));
        verify(connectionMock).close();
    }

    @Test
    public void testGetUserGames() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        assertEquals(0, gameDao.getUserGames("nika", 20).size());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(20));
        verify(preparedStatementMock, atLeastOnce()).setString(anyInt(), eq("nika"));
        verify(connectionMock).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetUserGamesNotZero() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetMock.getInt("GameID")).thenReturn(1).thenReturn(2);
        when(resultSetMock.getInt("AccID")).thenReturn(3).thenReturn(4);
        when(resultSetMock.getInt("ratingChange")).thenReturn(5).thenReturn(6);
        java.sql.Date date1 = new java.sql.Date(0);
        java.sql.Date date2 = new java.sql.Date(0);
        when(resultSetMock.getDate(anyObject())).thenReturn(date1).thenReturn(date2);

        List<Game> list = gameDao.getUserGames("nika", 10);
        assertEquals(2, list.size());

        assertEquals(1, list.get(0).getGameID());
        assertEquals(2, list.get(1).getGameID());

        assertEquals(5, list.get(0).getRatingChange());
        assertEquals(6, list.get(1).getRatingChange());

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(connectionMock).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetUserGamesNotZero1() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetMock.getInt("GameID")).thenReturn(1).thenReturn(1);
        when(resultSetMock.getInt("AccID")).thenReturn(3).thenReturn(4);
        when(resultSetMock.getInt("ratingChange")).thenReturn(5).thenReturn(6);
        java.sql.Date date1 = new java.sql.Date(0);
        java.sql.Date date2 = new java.sql.Date(0);
        when(resultSetMock.getDate(anyObject())).thenReturn(date1).thenReturn(date2);

        List<Game> list = gameDao.getUserGames("nika", 10);
        assertEquals(1, list.size());

        assertEquals(1, list.get(0).getGameID());
        assertEquals(2, list.get(0).getParticipantIDs().size());

        assertEquals(5, list.get(0).getRatingChange());

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(connectionMock).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetUserGamesException() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString()))
                .thenThrow(new SQLException());
        assertEquals(0, gameDao.getUserGames("nika", 20).size());
    }

    ///

    @Test
    public void testGetUserGamesByID() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        assertEquals(0, gameDao.getUserGamesByID(15, 20).size());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(20));
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(15));
        verify(connectionMock).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetUserGamesByIDNotZero() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetMock.getInt("GameID")).thenReturn(1).thenReturn(2);
        when(resultSetMock.getInt("AccID")).thenReturn(3).thenReturn(4);
        when(resultSetMock.getInt("ratingChange")).thenReturn(5).thenReturn(6);
        java.sql.Date date1 = new java.sql.Date(0);
        java.sql.Date date2 = new java.sql.Date(0);
        when(resultSetMock.getDate(anyObject())).thenReturn(date1).thenReturn(date2);

        List<Game> list = gameDao.getUserGamesByID(15, 2);
        assertEquals(2, list.size());

        assertEquals(1, list.get(0).getGameID());

        assertEquals(5, list.get(0).getRatingChange());

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(connectionMock).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetUserGamesByIDNotZero1() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetMock.getInt("GameID")).thenReturn(1).thenReturn(1);
        when(resultSetMock.getInt("AccID")).thenReturn(3).thenReturn(4);
        when(resultSetMock.getInt("ratingChange")).thenReturn(5).thenReturn(6);
        java.sql.Date date1 = new java.sql.Date(0);
        java.sql.Date date2 = new java.sql.Date(0);
        when(resultSetMock.getDate(anyObject())).thenReturn(date1).thenReturn(date2);

        List<Game> list = gameDao.getUserGamesByID(12, 10);
        assertEquals(1, list.size());

        assertEquals(1, list.get(0).getGameID());
        assertEquals(2, list.get(0).getParticipantIDs().size());

        assertEquals(5, list.get(0).getRatingChange());

        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(12));
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(10));

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(connectionMock).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetUserGamesByIDException() throws Exception {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString()))
                .thenThrow(new SQLException());
        assertEquals(0, gameDao.getUserGamesByID(10, 20).size());
    }

    @Test
    public void testAddParticipations() throws SQLException {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        gameDao.addParticipation(1, 2, 3, 4);
        verify(preparedStatementMock).setInt(anyInt(), eq(1));
        verify(preparedStatementMock).setInt(anyInt(), eq(2));
        verify(preparedStatementMock).setInt(anyInt(), eq(3));
        verify(preparedStatementMock).setInt(anyInt(), eq(4));

        verify(preparedStatementMock, atLeastOnce()).close();
        verify(connectionMock).close();
    }

    @Test
    public void testAddParticipationsException() throws SQLException {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        gameDao.addParticipation(1, 2, 3, 4);
        verify(connectionMock).close();
    }

    @Test
    public void testTotalGamesCount() throws SQLException {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(anyInt())).thenReturn(2);
        assertEquals(2, gameDao.getGamesCount());

        verify(preparedStatementMock).executeQuery();
        verify(connectionMock).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testTotalGamesCountException() throws SQLException {
        initMocks();
        GameDao gameDao = new GameDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(0, gameDao.getGamesCount());

        verify(connectionMock).close();
    }
}