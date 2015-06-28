package Core.Dao;

import Core.Bean.Message;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Created by Nika on 17:55, 6/27/2015.
 */
public class MessageDaoTest {
    private DataSource dataSourceMock;
    private Connection connectionMock;
    private PreparedStatement preparedStatementMock;
    private ResultSet resultSetMock;
    private Message message;

    private void initMocks() throws SQLException {
        dataSourceMock = mock(DataSource.class);
        connectionMock = mock(Connection.class);
        preparedStatementMock = mock(PreparedStatement.class);
        resultSetMock = mock(ResultSet.class);
        when(dataSourceMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    }

    private void initMessage() {
        message = new Message();
        message.setText("messageText");
        message.setHeader("messageHeader");
        message.setDate(new Date(System.currentTimeMillis()));
        message.setAccFrom(10);
        message.setAccTo(20);
        message.setType(Message.Type.GOTTEN);
    }

    @Test
    public void testSendMessageException() throws Exception {
        initMocks();
        initMessage();
        MessageDao messageDao = new MessageDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        assertEquals(false, messageDao.sendMessage(message));
    }


    @Test
    public void testSendMessage() throws Exception {
        initMocks();
        when(resultSetMock.next()).thenReturn(true);
        initMessage();
        MessageDao messageDao = new MessageDao(dataSourceMock);
        messageDao.sendMessage(message);

        verify(dataSourceMock).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());

        verify(preparedStatementMock, atLeastOnce()).setString(anyInt(), eq(message.getText()));
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(message.getAccFrom()));
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(message.getAccTo()));
        java.sql.Date date = new java.sql.Date(message.getDate().getTime());
        verify(preparedStatementMock, atLeastOnce()).setTimestamp(anyInt(), anyObject());
        verify(preparedStatementMock, atLeastOnce()).execute();
        verify(preparedStatementMock, atLeastOnce()).close();
        verify(connectionMock).close();
    }

    @Test
    public void testGetMessages() throws Exception {
        initMocks();
        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        initMessage();
        MessageDao messageDao = new MessageDao(dataSourceMock);
        messageDao.getMessages(5, 12);

        verify(dataSourceMock).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(5));
        verify(preparedStatementMock, atLeastOnce()).setInt(anyInt(), eq(12));


        verify(preparedStatementMock, atLeastOnce()).executeQuery();
        verify(preparedStatementMock, atLeastOnce()).close();
        verify(connectionMock).close();
    }

    @Test
    public void testGetMessagesException() throws Exception {
        initMocks();
        initMessage();
        MessageDao messageDao = new MessageDao(dataSourceMock);
        when(dataSourceMock.getConnection()).thenThrow(new SQLException());
        assertEquals(null, messageDao.getMessages(5, 12));
    }
}