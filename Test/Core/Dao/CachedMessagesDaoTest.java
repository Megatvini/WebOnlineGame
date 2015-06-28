package Core.Dao;

import Core.Bean.Message;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Nika on 04:13, 6/28/2015.
 */
public class CachedMessagesDaoTest {
    private DataSource dataSourceMock;
    private Connection connectionMock;
    private PreparedStatement preparedStatementMock;
    private ResultSet resultSetMock;
    private Message message1;
    private Message message2;
    private Map<String, List<Message>> messageMap;
    private List<Message> messageList;

    private void initMocks() throws SQLException {
        dataSourceMock = mock(DataSource.class);
        connectionMock = mock(Connection.class);
        preparedStatementMock = mock(PreparedStatement.class);
        resultSetMock = mock(ResultSet.class);
        when(dataSourceMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    }

    private void initMessageMap() {
        message1 = new Message();
        message1.setText("messageText1");
        message1.setHeader("messageHeader1");
        message1.setDate(new Date(System.currentTimeMillis()));
        message1.setAccFrom(1);
        message1.setAccTo(2);
        message1.setType(Message.Type.GOTTEN);

        message2 = new Message();
        message2.setText("messageText1");
        message2.setHeader("messageHeader1");
        message2.setDate(new Date(System.currentTimeMillis()));
        message2.setAccFrom(3);
        message2.setAccTo(4);
        message2.setType(Message.Type.SENT);

        messageList = new ArrayList<>();
        messageList.add(message1);
        messageList.add(message2);

        messageMap = new HashMap<>();
        messageMap.put("user1",messageList);
        messageMap.put("user2",messageList);
    }

    @Test
    public void testAddMessages() throws Exception {
        initMocks();
        initMessageMap();
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(dataSourceMock);
        assertTrue(cachedMessagesDao.addMessages(75, messageMap));

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(75));
        verify(preparedStatementMock, atLeastOnce()).setString(anyInt(), eq("user1"));
        verify(preparedStatementMock, atLeastOnce()).setString(anyInt(), eq("user2"));

        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
    }

    @Test
    public void testAddMessagesConnException() throws Exception {
        initMocks();
        initMessageMap();
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        assertFalse(cachedMessagesDao.addMessages(75, messageMap));
    }

    @Test
    public void testAddMessagesStException() throws Exception {
        initMocks();
        initMessageMap();
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        assertFalse(cachedMessagesDao.addMessages(75, messageMap));
        verify(connectionMock, atLeastOnce()).close();
    }

    @Test
    public void testAddSingleMessage() throws Exception {
        initMocks();
        initMessageMap();
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(dataSourceMock);
        assertTrue(cachedMessagesDao.addSingleMessage(12, "nika", "text", new Date()));

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(12));
        verify(preparedStatementMock, atLeastOnce()).setString(anyInt(), eq("nika"));
        verify(preparedStatementMock, atLeastOnce()).setString(anyInt(), eq("text"));
        verify(preparedStatementMock, atLeastOnce()).close();
        verify(connectionMock, atLeastOnce()).close();
    }

    @Test
    public void testAddSingleMessageConnException() throws Exception {
        initMocks();
        initMessageMap();
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        assertFalse(cachedMessagesDao.addSingleMessage(12, "nika", "text", new Date()));
    }


    @Test
    public void testAddSingleMessageStException() throws Exception {
        initMocks();
        initMessageMap();
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        assertFalse(cachedMessagesDao.addSingleMessage(12, "nika", "text", new Date()));
        verify(connectionMock, atLeastOnce()).close();
    }

    @Test
    public void testTakeMessages() throws Exception {
        initMocks();
        initMessageMap();
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(dataSourceMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Map<String, List<Message>> res = cachedMessagesDao.takeMessages(12);
        assertEquals(1, res.size());

        verify(connectionMock, atLeastOnce()).close();
        verify(preparedStatementMock, atLeastOnce()).close();
        verify(connectionMock, atLeastOnce()).commit();
    }

    @Test
    public void testTakeMessagesConnException() throws Exception {
        initMocks();
        initMessageMap();
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        assertEquals(null, cachedMessagesDao.takeMessages(12));
    }

    @Test
    public void testTakeMessagesFirstSTException() throws Exception {
        initMocks();
        initMessageMap();
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException());
        assertEquals(null, cachedMessagesDao.takeMessages(12));
        verify(connectionMock, atLeastOnce()).close();
        verify(connectionMock, atLeastOnce()).rollback();
    }

    @Test
    public void testTakeMessagesSecondSTException() throws Exception {
        initMocks();
        initMessageMap();
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(dataSourceMock);
        when(connectionMock.prepareStatement(anyString())).
                thenReturn(preparedStatementMock).thenThrow(new SQLException());
        assertEquals(null, cachedMessagesDao.takeMessages(12));
        verify(connectionMock, atLeastOnce()).close();
        verify(connectionMock, atLeastOnce()).rollback();
    }
}