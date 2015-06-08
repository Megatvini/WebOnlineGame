package Game.Controller;

import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
/**
 * Created by Nika on 01:35, 6/8/2015.
 */
public class GameServerTest {
    private String initMessage =
            "{ \"type\":\"init\",\n" +
                    "\"name\":\"rezo\"\n" +
                    "}";
    private String updateMessage =
            "{" +
                    "        \"type\":\"update\",\n" +
                    "        \"name\":\"rezo\",\n" +
                    "        \"coordinates\": {\n" +
                    "                \"x\": 10,\n" +
                    "                \"y\": 20\n" +
                    "        }\n" +
                    "}";

    @Test
    public void testOnMessage() throws Exception {
        GameManager gameManager = mock(GameManager.class);
        GameServer gameServer = new GameServer(gameManager);
        Session player1Session = mock(Session.class);
        gameServer.onMessage(initMessage, player1Session);
        verify(gameManager).addPlayer(eq("rezo"), any());
        verify(player1Session).getBasicRemote();
    }

    @Test
    public void testOnMessage1() throws Exception {
        GameManager gameManager = mock(GameManager.class);
        GameServer gameServer = new GameServer(gameManager);
        Session player1Session = mock(Session.class);
        gameServer.onMessage(initMessage, player1Session);
        gameServer.onMessage(updateMessage, player1Session);

        verify(gameManager).addPlayer(eq("rezo"), any());
        verify(player1Session).getBasicRemote();
    }

    @Test
    public void testOnClose() throws Exception {
        GameManager gameManager = mock(GameManager.class);
        GameServer gameServer = new GameServer(gameManager);
        Session player1Session = mock(Session.class);
        gameServer.onMessage(initMessage, player1Session);
        gameServer.onMessage(updateMessage, player1Session);
        gameServer.onClose(player1Session);
        verify(gameManager).removePlayer("rezo");
        gameServer.onError(player1Session, new Throwable());
    }

    @Test
    public void testWithRoomMates() {
        GameServer gameServer = new GameServer();

        HttpSession httpSessionMock = mock(HttpSession.class);
        Map roomMates = mock(Map.class);
        ServletContext servletContextMock = mock(ServletContext.class);
        EndpointConfig configMock = mock(EndpointConfig.class);
        Map userPropertiesMock = mock(Map.class);

        when(configMock.getUserProperties()).thenReturn(userPropertiesMock);
        when(userPropertiesMock.get("httpSession")).thenReturn(httpSessionMock);
        when(httpSessionMock.getServletContext()).thenReturn(servletContextMock);
        when(servletContextMock.getAttribute("roomMates")).thenReturn(roomMates);

        Session sessionMock = mock(Session.class);

        gameServer.open(sessionMock, configMock);
        gameServer.open(sessionMock, configMock);

        verify(configMock).getUserProperties();
        verify(userPropertiesMock).get("httpSession");
        verify(httpSessionMock).getServletContext();
        verify(servletContextMock).getAttribute("roomMates");
    }
}