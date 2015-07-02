package Game.Controller;

import org.junit.Test;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by Nika on 02:39, 6/7/2015.
 */
public class UserConnectorTest {

    @Test
    public void testAddUser() throws Exception {
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);
        Session sessionMock = mock(Session.class);
        when(sessionMock.getBasicRemote()).thenReturn(basic);
        assertTrue(connector.addUser("player1", sessionMock));
        assertTrue(connector.addUser("player2", sessionMock));
        assertTrue(connector.addUser("player3", sessionMock));
        assertFalse(connector.addUser("player3", sessionMock));
        assertFalse(connector.addUser("player1", sessionMock));
    }

    @Test
    public void testRemoveUser() throws Exception {
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);
        Session sessionMock = mock(Session.class);
        when(sessionMock.getBasicRemote()).thenReturn(basic);

        assertTrue(connector.addUser("player1", sessionMock));
        assertTrue(connector.addUser("player2", sessionMock));

        assertTrue(connector.removeUser("player1"));
        assertFalse(connector.removeUser("player1"));
        assertFalse(connector.removeUser("player3"));

        assertTrue(connector.removeUser("player2"));
        assertFalse(connector.removeUser("player2"));

        assertTrue(connector.addUser("player1", sessionMock));
    }

    @Test
    public void combinedAddRemove() {
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);
        Session sessionMock = mock(Session.class);
        when(sessionMock.getBasicRemote()).thenReturn(basic);

        for (int i=0; i<100; i++) {
            assertTrue(connector.addUser(i + "", sessionMock));
            assertFalse(connector.addUser(i + "", sessionMock));
        }

        for (int i=0; i<100; i++) {
            assertTrue(connector.removeUser(i + ""));
            assertFalse(connector.removeUser(i + ""));
        }
    }

    @Test
    public void testSendMessage() throws Exception{
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);
        Session sessionMock = mock(Session.class);
        when(sessionMock.getBasicRemote()).thenReturn(basic);
        when(sessionMock.isOpen()).thenReturn(true);
        connector.addUser("nika", sessionMock);
        connector.sendMessageTo("nika", "message");
        verify(basic).sendText("message");
    }

    @Test
    public void testSendMessageTo1() throws Exception {
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic basic1 = mock(RemoteEndpoint.Basic.class);
        RemoteEndpoint.Basic basic2 = mock(RemoteEndpoint.Basic.class);

        Session sessionMock1 = mock(Session.class);
        when(sessionMock1.getBasicRemote()).thenReturn(basic1);
        when(sessionMock1.isOpen()).thenReturn(true);

        Session sessionMock2 = mock(Session.class);
        when(sessionMock2.getBasicRemote()).thenReturn(basic2);
        when(sessionMock2.isOpen()).thenReturn(true);


        connector.addUser("nika1", sessionMock1);
        connector.addUser("nika2", sessionMock2);
        connector.sendMessageTo("nika1", "message");


        verify(basic1).sendText("message");
        verify(basic1, never()).sendText("something");
        verify(basic2, never()).sendText(anyString());

        connector.sendMessageTo("nika2", "message");
        connector.sendMessageTo("nika2", "message");
        verify(basic2, times(2)).sendText("message");
    }

    @Test
    public void testSendMessageTo2() throws Exception {
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic oldBasic = mock(RemoteEndpoint.Basic.class);
        RemoteEndpoint.Basic newBasic = mock(RemoteEndpoint.Basic.class);

        Session oldSession = mock(Session.class);
        when(oldSession.getBasicRemote()).thenReturn(oldBasic);
        when(oldSession.isOpen()).thenReturn(true);

        Session newSession = mock(Session.class);
        when(newSession.getBasicRemote()).thenReturn(newBasic);
        when(newSession.isOpen()).thenReturn(true);



        assertTrue(connector.addUser("nika1", oldSession));
        connector.sendMessageTo("nika1", "message");
        verify(oldBasic).sendText("message");

        assertFalse(connector.addUser("nika1", newSession));
        connector.sendMessageTo("nika1", "someText");

        verify(oldBasic, never()).sendText("someText");
        verify(newBasic).sendText("someText");
    }

    @Test
    public void testSendMessageTo3() throws Exception {
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);
        Session session = mock(Session.class);
        when(session.getBasicRemote()).thenReturn(basic);
        when(session.isOpen()).thenReturn(true);

        doThrow(new IOException()).when(basic).sendText("text");
        connector.addUser("nika", session);
        assertFalse(connector.sendMessageTo("nika", "text"));
    }

    @Test
    public void testSendMessageTo4() throws Exception {
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);
        Session session = mock(Session.class);
        when(session.getBasicRemote()).thenReturn(basic);
        when(session.isOpen()).thenReturn(false);

        connector.addUser("nika", session);
        assertFalse(connector.sendMessageTo("nika", "text"));
    }
}