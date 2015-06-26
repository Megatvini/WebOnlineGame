package Core.Dao;

import Core.Bean.Account;
import Interfaces.iAccount;
import Interfaces.iProfile;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
        Date date = new Date();
        account.setBirthDate(date);
    }

    @Test
    public void testRegisterUser() throws Exception {
        initMocks();
        initAccount();
        AccountDao accountDao = new AccountDao(dataSourceMock);
        assertTrue(accountDao.registerUser(account));

        verify(dataSourceMock).getConnection();
        verify(connectionMock).prepareStatement(anyString());

        verify(preparedStatementMock, times(7)).setString(anyInt(), anyString());
        verify(preparedStatementMock).setInt(anyInt(), anyInt());
        verify(preparedStatementMock).setDate(anyInt(), any());
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
        verify(dataSourceMock).getConnection();
        verify(connectionMock).prepareStatement(anyString());

        verify(preparedStatementMock, times(5)).setString(anyInt(), anyString());
        verify(preparedStatementMock, times(2)).setInt(anyInt(), anyInt());
        verify(preparedStatementMock).setDate(anyInt(), any());
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

        verify(dataSourceMock).getConnection();
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock).next();

        verify(resultSetMock, times(7)).getString(anyString());
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
    public void testGetUsersLike() throws Exception {
        initAccount();
        initMocks();

        AccountDao accountDao = new AccountDao(dataSourceMock);
        accountDao.getUsersLike("nika");

        verify(dataSourceMock).getConnection();
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
        assertEquals(null, accountDao.getUsersLike("nika"));
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
}