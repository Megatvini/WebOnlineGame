package Core.Dao;

import Core.Bean.Account;
import Interfaces.iAccount;
import Interfaces.iProfile;
import org.junit.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Nika on 04:05, 6/26/2015.
 */
public class AccountDaoTest {
    private DataSource dataSourceMock;
    private Connection connectionMock;
    private PreparedStatement preparedStatementMock;
    private ResultSet resultSetMock;
    private iAccount account;

    private void initMocks() throws SQLException {
        dataSourceMock = mock(DataSource.class);
        connectionMock = mock(Connection.class);
        preparedStatementMock = mock(PreparedStatement.class);
        resultSetMock = mock(ResultSet.class);
        when(dataSourceMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    }

    private void initAccount() {
        account = new Account();
        account.setID(1654);
        account.setNickname("megatvini");
        account.setFirstName("nika");
        account.setLastName("doghonadze");
        account.setGender(iProfile.Gender.MALE);
        account.setMail("ndogh13@freeuni.edu.ge");
        account.setAbout("text");
        account.setPicturePath("c:\\path\\picture.jpg");
        account.setRating(1554);
        Date date = new Date(System.currentTimeMillis());
        account.setBirthDate(date);
    }

    @Test
    public void testRegisterUser() throws Exception {
        initMocks();
        initAccount();
        AccountDao accountDao = new AccountDao(dataSourceMock);
        assertTrue(accountDao.registerUser(account));

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock).prepareStatement(anyString());

        verify(preparedStatementMock).setString(anyInt(), eq(account.getNickname()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getPassword()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getPicturePath()));
        verify(preparedStatementMock).setInt(anyInt(), eq(account.getRating()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getFirstName()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getLastName()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getAbout()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getMail()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getGender() == iProfile.Gender.FEMALE ? "female" : "male"));
        java.sql.Date date = new java.sql.Date(account.getBirthDate().getTime());
        verify(preparedStatementMock).setDate(anyInt(), eq(date));
        verify(preparedStatementMock).execute();

        verify(preparedStatementMock).close();
        verify(connectionMock).close();
    }

    @Test
    public void testRegisterUser1() throws Exception {
        initMocks();
        initAccount();
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        AccountDao accountDao = new AccountDao(dataSourceMock);
        assertFalse(accountDao.registerUser(account));
    }

    @Test
    public void testChangeUser() throws Exception {
        initMocks();
        initAccount();
        AccountDao accountDao = new AccountDao(dataSourceMock);
        accountDao.changeUser(account);
        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock).prepareStatement(anyString());

        verify(preparedStatementMock).setString(anyInt(), eq(account.getPassword()));
        verify(preparedStatementMock).setInt(anyInt(), eq(account.getRating()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getFirstName()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getLastName()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getAbout()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getPicturePath()));
        verify(preparedStatementMock).setString(anyInt(), eq(account.getGender() == iProfile.Gender.FEMALE ? "female" : "male"));
        java.sql.Date date = new java.sql.Date(account.getBirthDate().getTime());
        verify(preparedStatementMock).setDate(anyInt(), eq(date));
        verify(preparedStatementMock).execute();

        verify(preparedStatementMock).close();
        verify(connectionMock).close();
    }

    @Test
    public void testChangeUser1() throws Exception {
        initMocks();
        initAccount();
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        AccountDao accountDao = new AccountDao(dataSourceMock);
        assertFalse(accountDao.changeUser(account));
    }

    @Test
    public void testChangeUser2() throws Exception {
        initMocks();
        initAccount();
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        AccountDao accountDao = new AccountDao(dataSourceMock);
        assertFalse(accountDao.changeUser(account));
    }

    @Test
    public void testGetUser() throws Exception {
        initMocks();
        initAccount();
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getString("Gender")).thenReturn("Male");

        AccountDao accountDao = new AccountDao(dataSourceMock);
        accountDao.getUser("nika");

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setString(anyInt(), eq("nika"));
        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock).next();

        //must get all parameters from database
        verify(resultSetMock, times(8)).getString(anyString());
        verify(resultSetMock, times(2)).getInt(anyString());
        verify(resultSetMock).getDate(anyString());

        verify(connectionMock).close();
        verify(preparedStatementMock).close();
    }

    @Test
    public void testGetUser1() throws Exception {
        initMocks();
        initAccount();
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        AccountDao accountDao = new AccountDao(dataSourceMock);
        assertEquals(null, accountDao.getUser("nika"));
    }

    @Test
    public void testGetUserID() throws Exception {
        initMocks();
        initAccount();
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getString("Gender")).thenReturn("Male");

        AccountDao accountDao = new AccountDao(dataSourceMock);
        accountDao.getUser(5);

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setInt(anyInt(), eq(5));
        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock).next();

        //must get all parameters from database
        verify(resultSetMock, times(8)).getString(anyString());
        verify(resultSetMock, times(2)).getInt(anyString());
        verify(resultSetMock).getDate(anyString());

        verify(connectionMock).close();
        verify(preparedStatementMock).close();
    }

    @Test
    public void testGetUserID1() throws Exception {
        initMocks();
        initAccount();
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        AccountDao accountDao = new AccountDao(dataSourceMock);
        assertEquals(null, accountDao.getUser(5));
    }

    @Test
    public void testGetUsersLike() throws Exception {
        initAccount();
        initMocks();

        AccountDao accountDao = new AccountDao(dataSourceMock);
        accountDao.getUsersLike("nika");

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock).next();


        verify(connectionMock).close();
        verify(preparedStatementMock).close();
    }

    @Test
    public void testGetUsersLike2() throws Exception {
        initAccount();
        initMocks();

        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        AccountDao accountDao = new AccountDao(dataSourceMock);
        assertEquals(0, accountDao.getUsersLike("nika").size());
    }

    @Test
    public void testGetUsersLike3() throws Exception {
        initAccount();
        initMocks();

        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getString("Nickname")).thenReturn("nika");
        AccountDao accountDao = new AccountDao(dataSourceMock);

        Set<String> set = accountDao.getUsersLike("nika");

        assertEquals(1, set.size());
        assertTrue(set.contains("nika"));
    }

    @Test
    public void testGetUsersLike4() throws SQLException {
        initAccount();
        initMocks();

        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getString("Nickname")).thenReturn("nika");
        AccountDao accountDao = new AccountDao(dataSourceMock);

        accountDao.getUsersLike("na", 5, 6);

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).executeQuery();
        verify(preparedStatementMock).setString(anyInt(), anyString());
        verify(preparedStatementMock).setInt(anyInt(), eq(30));
        verify(preparedStatementMock).setInt(anyInt(), eq(36));

        verify(resultSetMock).getString("Nickname");
        verify(preparedStatementMock).close();
        verify(connectionMock).close();
    }

    @Test
    public void testGetUsersLike5Exception() throws SQLException {
        initAccount();
        initMocks();

        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        AccountDao accountDao = new AccountDao(dataSourceMock);
        assertEquals(0, accountDao.getUsersLike("nika", 11, 20).size());
    }

    @Test
    public void testGetUsersCount() throws SQLException {
        initAccount();
        initMocks();
        AccountDao accountDao = new AccountDao(dataSourceMock);

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(anyInt())).thenReturn(12);
        int count = accountDao.getUsersCount();
        assertEquals(12, count);

        verify(preparedStatementMock).close();
        verify(connectionMock).close();
    }

    @Test
    public void testGetUsersCount1() throws SQLException {
        initAccount();
        initMocks();
        AccountDao accountDao = new AccountDao(dataSourceMock);

        when(resultSetMock.next()).thenReturn(false);
        int count = accountDao.getUsersCount();
        assertEquals(0, count);

        verify(preparedStatementMock).close();
        verify(connectionMock).close();
    }

    @Test
    public void testGetUsersCount2() throws SQLException {
        initAccount();
        initMocks();

        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        when(resultSetMock.next()).thenReturn(true);
        AccountDao accountDao = new AccountDao(dataSourceMock);

        assertEquals(0, accountDao.getUsersCount());
    }

    @Test
    public void testGetUsersIntervalByRating() throws SQLException {
        initAccount();
        initMocks();

        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetMock.getString("Nickname")).thenReturn("nika").thenReturn("guka");
        AccountDao accountDao = new AccountDao(dataSourceMock);

        List<String> list = accountDao.getUsersIntervalByRating(1, 20);

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).executeQuery();
        verify(preparedStatementMock).setInt(anyInt(), eq(20));
        verify(preparedStatementMock).setInt(anyInt(), eq(40));
        verify(resultSetMock, times(3)).next();
        verify(preparedStatementMock).close();
        verify(connectionMock).close();

        assertEquals(2, list.size());
        assertTrue(list.get(0).equals("nika"));
        assertTrue(list.get(1).equals("guka"));
    }

    @Test
    public void testGetUsersIntervalByRatingException() throws SQLException {
        initAccount();
        initMocks();

        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        AccountDao accountDao = new AccountDao(dataSourceMock);

        List<String> list = accountDao.getUsersIntervalByRating(1, 20);
        assertEquals(0, list.size());
    }
}