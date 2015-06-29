package Core.Dao;

import Interfaces.iAccount;
import org.junit.Test;

import javax.sql.DataSource;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

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
        assertTrue(friendsDao.confirmFriendRequest(12, 14));
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
        assertFalse(friendsDao.confirmFriendRequest(12, 14));
    }

    @Test
    public void testConfirmFriendRequestSTException() throws Exception {
        initMocks();
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertFalse(friendsDao.confirmFriendRequest(12, 14));
        verify(connectionMock).close();
    }

    @Test
    public void testConfirmFriendRequestSTException1() throws Exception {
        initMocks();
        when(connectionMock.prepareStatement(anyString())).
                thenReturn(preparedStatementMock).thenThrow(new SQLException());
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertFalse(friendsDao.confirmFriendRequest(12, 14));
        verify(connectionMock).close();
    }

    @Test
    public void testConfirmFriendRequestSTException2() throws Exception {
        initMocks();
        when(connectionMock.prepareStatement(anyString()))
                .thenReturn(preparedStatementMock)
                .thenReturn(preparedStatementMock)
                .thenThrow(new SQLException());
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertFalse(friendsDao.confirmFriendRequest(12, 14));
        verify(connectionMock).close();
    }

    @Test
    public void testRejectFriendRequest() throws SQLException {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertTrue(friendsDao.rejectFriendRequest(12, 14));
        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(12));
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(14));

        verify(preparedStatementMock, atLeastOnce()).execute();
        verify(connectionMock).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testRejectFriendRequestConnException() throws SQLException {
        initMocks();
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertFalse(friendsDao.rejectFriendRequest(12, 14));
    }


    @Test
    public void testRejectFriendRequestStException() throws SQLException {
        initMocks();
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        assertFalse(friendsDao.rejectFriendRequest(12, 14));
        verify(connectionMock).close();
    }


    @Test
    public void testGetFriendRequestsFrom() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetMock.getString(any())).thenReturn("nika").thenReturn("rezo");
        Set<String> friendRequestsFrom = friendsDao.getFriendRequestsFrom(15);
        //assertNotEquals(null, friendRequestsFrom);
        assertTrue(friendRequestsFrom.contains("nika"));
        assertTrue(friendRequestsFrom.contains("rezo"));


        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(15));

        verify(preparedStatementMock, atLeastOnce()).executeQuery();
        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetFriendRequestsFromConnException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        Set<String> friendRequestsFrom = friendsDao.getFriendRequestsFrom(15);
        assertEquals(null, friendRequestsFrom);
    }

    @Test
    public void testGetFriendRequestsFromStmException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        Set<String> friendRequestsFrom = friendsDao.getFriendRequestsFrom(15);
        assertEquals(null, friendRequestsFrom);
        verify(connectionMock).close();
    }

    @Test
    public void testGetFriendRequestsFrom1() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetMock.getString(any())).thenReturn("nika").thenReturn("rezo");
        Set<String> friendRequestsFrom = friendsDao.getFriendRequestsFrom("asd");
        //assertNotEquals(null, friendRequestsFrom);

        assertTrue(friendRequestsFrom.contains("nika"));
        assertTrue(friendRequestsFrom.contains("rezo"));


        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setString(anyInt(), eq("asd"));

        verify(preparedStatementMock, atLeastOnce()).executeQuery();
        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetFriendRequestsFrom1ConnException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        Set<String> friendRequestsFrom = friendsDao.getFriendRequestsFrom("nika");
        assertEquals(null, friendRequestsFrom);
    }

    @Test
    public void testGetFriendRequestsFrom1StmException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        Set<String> friendRequestsFrom = friendsDao.getFriendRequestsFrom("nika");
        assertEquals(null, friendRequestsFrom);
        verify(connectionMock).close();
    }

    @Test
    public void testGetFriendRequestsTo() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetMock.getString(any())).thenReturn("one").thenReturn("two");

        Set<String> friendRequestsFrom = friendsDao.getFriendRequestsTo("asdasdasd");
        //assertNotEquals(null, friendRequestsFrom);

        assertTrue(friendRequestsFrom.contains("one"));
        assertTrue(friendRequestsFrom.contains("two"));

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setString(anyInt(), eq("asdasdasd"));

        verify(preparedStatementMock, atLeastOnce()).executeQuery();
        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetFriendRequestsToConnException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        assertEquals(null, friendsDao.getFriendRequestsTo("nika"));
    }

    @Test
    public void testGetFriendRequestsToSTException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(null, friendsDao.getFriendRequestsTo("nika"));
        verify(connectionMock).close();
    }

    @Test
    public void testGetFriendRequestsTo1() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetMock.getString(any())).thenReturn("one").thenReturn("two");

        Set<String> friendRequestsFrom = friendsDao.getFriendRequestsTo(12);
        //assertNotEquals(null, friendRequestsFrom);

        assertTrue(friendRequestsFrom.contains("one"));
        assertTrue(friendRequestsFrom.contains("two"));

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(12));

        verify(preparedStatementMock, atLeastOnce()).executeQuery();
        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetFriendRequestsTo1ConnException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        assertEquals(null, friendsDao.getFriendRequestsTo(15));
    }

    @Test
    public void testGetFriendRequestsTo1StException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(null, friendsDao.getFriendRequestsTo(15));
        verify(connectionMock).close();
    }


    @Test
    public void testGetFriendNames() throws Exception {
        initMocks();

        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetMock.getString(any())).thenReturn("one").thenReturn("two");

        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        Set<String> friends = friendsDao.getFriendNames("nika");

        //assertNotEquals(null, friends);

        assertTrue(friends.contains("one"));
        assertTrue(friends.contains("two"));

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setString(anyInt(), eq("nika"));

        verify(preparedStatementMock, atLeastOnce()).executeQuery();
        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetFriendNamesConnException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        Set<String> friends = friendsDao.getFriendNames("nika");
        assertEquals(null, friends);
    }

    @Test
    public void testGetFriendNamesSTException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        Set<String> friends = friendsDao.getFriendNames("nika");
        assertEquals(null, friends);
        verify(connectionMock).close();
    }


    @Test
    public void testGetFriendNamesByID() throws Exception {
        initMocks();

        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSetMock.getString(any())).thenReturn("one").thenReturn("two");

        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        Set<String> friends = friendsDao.getFriendNamesByID(15);

        //assertNotEquals(null, friends);

        assertTrue(friends.contains("one"));
        assertTrue(friends.contains("two"));

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(15));

        verify(preparedStatementMock, atLeastOnce()).executeQuery();
        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testGetFriendNamesByIDConnException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        Set<String> friends = friendsDao.getFriendNamesByID(12);
        assertEquals(null, friends);
    }

    @Test
    public void testGetFriendNamesByIDStException() throws Exception {
        initMocks();
        FriendsDao friendsDao = new FriendsDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        Set<String> friends = friendsDao.getFriendNamesByID(12);
        assertEquals(null, friends);
        verify(connectionMock).close();
    }
}