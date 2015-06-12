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

    private HttpSession httpSessionMock;
    private Map roomMates;
    private ServletContext servletContextMock;
    private EndpointConfig configMock;
    private Map userPropertiesMock;
    private Session sessionMock;

    private void initMocks() {
        httpSessionMock = mock(HttpSession.class);
        roomMates = mock(Map.class);
        servletContextMock = mock(ServletContext.class);
        configMock = mock(EndpointConfig.class);
        userPropertiesMock = mock(Map.class);
        sessionMock = mock(Session.class);

        when(configMock.getUserProperties()).thenReturn(userPropertiesMock);
        when(userPropertiesMock.get("httpSession")).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("userName")).thenReturn("rezo");
        when(httpSessionMock.getServletContext()).thenReturn(servletContextMock);
        when(servletContextMock.getAttribute("roomMates")).thenReturn(roomMates);
    }

    @Test
    public void testOnMessage() throws Exception {
        GameManager gameManager = mock(GameManager.class);
        GameServer gameServer = new GameServer(gameManager);
        initMocks();

        gameServer.open(sessionMock, configMock);
        gameServer.onMessage(initMessage, sessionMock);
        verify(gameManager).addPlayer(eq("rezo"), any());
    }

    @Test
    public void testOnMessage1() throws Exception {
        GameManager gameManager = mock(GameManager.class);
        GameServer gameServer = new GameServer(gameManager);
        initMocks();

        gameServer.open(sessionMock, configMock);
        gameServer.onMessage(initMessage, sessionMock);
        gameServer.onMessage(updateMessage, sessionMock);

        verify(gameManager).addPlayer(eq("rezo"), any());
    }

    @Test
    public void testOnClose() throws Exception {
        GameManager gameManagerMock = mock(GameManager.class);
        GameServer gameServer = new GameServer(gameManagerMock);
        initMocks();

        gameServer.open(sessionMock, configMock);
        gameServer.onMessage(initMessage, sessionMock);
        gameServer.onMessage(updateMessage, sessionMock);
        gameServer.onClose(sessionMock);
        verify(gameManagerMock).removePlayer("rezo");
        gameServer.onError(sessionMock, new Throwable());
    }

    @Test
    public void testWithRoomMates() {
        GameServer gameServer = new GameServer();
        initMocks();

        gameServer.open(sessionMock, configMock);
        gameServer.open(sessionMock, configMock);

        verify(configMock, times(2)).getUserProperties();
        verify(userPropertiesMock, times(2)).get("httpSession");
        verify(httpSessionMock).getServletContext();
        verify(servletContextMock).getAttribute("roomMates");
    }
}