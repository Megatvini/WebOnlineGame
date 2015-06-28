package Core.Dao;

import Interfaces.iAccount;
import org.junit.Test;

import javax.sql.DataSource;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by Nika on 04:55, 6/26/2015.
 */
public class FriendsDaoTest {
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


    @Test
    public void testAddFriend() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertTrue(friendsDao.addFriend(1, 2));
        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(1));
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(2));

        verify(preparedStatementMock, atLeastOnce()).execute();
        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testAddFriendConnException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());

        assertFalse(friendsDao.addFriend(1, 2));
    }

    @Test
    public void testAddFriendStException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        assertFalse(friendsDao.addFriend(1, 2));
        verify(connectionMock).close();
    }

    @Test
    public void testRemoveFriend() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertTrue(friendsDao.removeFriend(1, 2));

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(1));
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(2));

        verify(preparedStatementMock, atLeastOnce()).execute();
        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testRemoveFriendConnException() throws Exception {
        initMocks();
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertFalse(friendsDao.removeFriend(1, 2));
    }

    @Test
    public void testRemoveFriendSTException() throws Exception {
        initMocks();
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertFalse(friendsDao.removeFriend(1, 2));
        verify(connectionMock).close();
    }

    @Test
    public void testAddFriendRequest() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertTrue(friendsDao.addFriendRequest(3, 4));
        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(3));
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(4));

        verify(preparedStatementMock, atLeastOnce()).execute();
        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testAddFriendRequestConnException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        assertFalse(friendsDao.addFriendRequest(3, 4));
    }

    @Test
    public void testAddFriendRequestSTException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        assertFalse(friendsDao.addFriendRequest(3, 4));
        verify(connectionMock, atLeastOnce()).close();
    }

    @Test
    public void testConfirmFriendRequest() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertTrue(friendsDao.addFriendRequest(12, 14));
        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(12));
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(14));

        verify(preparedStatementMock, atLeastOnce()).execute();
        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testConfirmFriendRequestConnException() throws Exception {
        initMocks();
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertFalse(friendsDao.addFriendRequest(12, 14));
    }

    @Test
    public void testConfirmFriendRequestSTException() throws Exception {
        initMocks();
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertFalse(friendsDao.addFriendRequest(12, 14));
        verify(connectionMock).close();
    }

    @Test
    public void testGetFriendRequestsFrom() throws Exception {

    }

    @Test
    public void testGetFriendRequestsFrom1() throws Exception {

    }

    @Test
    public void testGetFriendRequestsTo() throws Exception {

    }

    @Test
    public void testGetFriendRequestsTo1() throws Exception {

    }

    @Test
    public void testGetFriendNames() throws Exception {

    }

    @Test
    public void testGetFriendNamesByID() throws Exception {

    }
}