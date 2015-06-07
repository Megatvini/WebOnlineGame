package Game.Controller;

import org.junit.Test;

import javax.websocket.RemoteEndpoint;
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
        assertTrue(connector.addUser("player1", basic));
        assertTrue(connector.addUser("player2", basic));
        assertTrue(connector.addUser("player3", basic));
        assertFalse(connector.addUser("player3", basic));
        assertFalse(connector.addUser("player1", basic));
    }

    @Test
    public void testRemoveUser() throws Exception {
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);
        assertTrue(connector.addUser("player1", basic));
        assertTrue(connector.addUser("player2", basic));

        assertTrue(connector.removeUser("player1"));
        assertFalse(connector.removeUser("player1"));
        assertFalse(connector.removeUser("player3"));

        assertTrue(connector.removeUser("player2"));
        assertFalse(connector.removeUser("player2"));

        assertTrue(connector.addUser("player1", basic));
    }

    @Test
    public void combinedAddRemove() {
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);
        for (int i=0; i<100; i++) {
            assertTrue(connector.addUser(i + "", basic));
            assertFalse(connector.addUser(i + "", basic));
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
        connector.addUser("nika", basic);
        connector.sendMessageTo("nika", "message");
        verify(basic).sendText("message");
    }

    @Test
    public void testSendMessageTo1() throws Exception {
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic basic1 = mock(RemoteEndpoint.Basic.class);
        RemoteEndpoint.Basic basic2 = mock(RemoteEndpoint.Basic.class);
        connector.addUser("nika1", basic1);
        connector.addUser("nika2", basic2);
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

        assertTrue(connector.addUser("nika1", oldBasic));
        connector.sendMessageTo("nika1", "message");
        verify(oldBasic).sendText("message");

        assertFalse(connector.addUser("nika1", newBasic));
        connector.sendMessageTo("nika1", "someText");

        verify(oldBasic, never()).sendText("someText");
        verify(newBasic).sendText("someText");
    }

    @Test
    public void testSendMessageTo3() throws Exception {
        UserConnector connector = new UserConnector();
        RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);
        doThrow(new IOException()).when(basic).sendText("text");
        connector.addUser("nika", basic);
        assertFalse(connector.sendMessageTo("nika", "text"));
    }
}