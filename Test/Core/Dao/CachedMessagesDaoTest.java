package Core.Dao;

import Core.Bean.Message;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        messageMap.put("user1",messageList);
        messageMap.put("user2",messageList);
    }

    @Test
    public void testAddMessages() throws Exception {
        initMocks();
        initMessageMap();
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(dataSourceMock);
        cachedMessagesDao.addMessages(2, messageMap);

        verify(dataSourceMock, atLeastOnce()).getConnection();
        verify(connectionMock, atLeastOnce()).prepareStatement(anyString());
        verify(preparedStatementMock).setInt(anyInt(), eq(2));
        verify(preparedStatementMock).setString(anyInt(), eq("user1"));
        verify(preparedStatementMock).setString(anyInt(), eq("user2"));


    }

    @Test
    public void testAddSingleMessage() throws Exception {

    }

    @Test
    public void testTakeMessages() throws Exception {

    }
}