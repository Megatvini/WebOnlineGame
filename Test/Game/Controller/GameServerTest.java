package Game.Controller;

import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.util.Map;

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
    private GameManager gameManagerMock;

    private void initMocks() {
        httpSessionMock = mock(HttpSession.class);
        roomMates = mock(Map.class);
        servletContextMock = mock(ServletContext.class);
        configMock = mock(EndpointConfig.class);
        userPropertiesMock = mock(Map.class);
        sessionMock = mock(Session.class);
        gameManagerMock = mock(GameManager.class);

        when(configMock.getUserProperties()).thenReturn(userPropertiesMock);
        when(userPropertiesMock.get(HttpSession.class.getName())).thenReturn(httpSessionMock);
        when(httpSessionMock.getAttribute("userName")).thenReturn("rezo");
        when(httpSessionMock.getServletContext()).thenReturn(servletContextMock);
        when(servletContextMock.getAttribute(GameManager.class.getName())).thenReturn(gameManagerMock);
    }

    @Test
    public void testOnMessage() throws Exception {
        GameServer gameServer = new GameServer();
        initMocks();

        gameServer.open(sessionMock, configMock);
        gameServer.onMessage(initMessage, sessionMock);
        verify(gameManagerMock).addPlayer(eq("rezo"), any());
    }

    @Test
    public void testOnMessage1() throws Exception {
        GameServer gameServer = new GameServer();
        initMocks();

        gameServer.open(sessionMock, configMock);
        gameServer.onMessage(initMessage, sessionMock);
        gameServer.onMessage(updateMessage, sessionMock);

        verify(gameManagerMock).addPlayer(eq("rezo"), any());
    }

    @Test
    public void testOnClose() throws Exception {
        GameServer gameServer = new GameServer();
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
        initMocks();
        GameServer gameServer = new GameServer();

        gameServer.open(sessionMock, configMock);
        gameServer.open(sessionMock, configMock);

        verify(configMock, times(2)).getUserProperties();
        verify(userPropertiesMock, times(2)).get(HttpSession.class.getName());
        verify(httpSessionMock).getServletContext();
        verify(servletContextMock).getAttribute(GameManager.class.getName());
    }
}