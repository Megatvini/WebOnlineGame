package Game.Controller;

import Core.Controller.RatingManager;
import Game.Model.GameWorld;
import org.junit.Test;

import javax.json.JsonObject;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by Nika on 18:37, 6/7/2015.
 */
public class GameManagerTest {
    private Map<String, Collection<String>> roomMates;
    private ScheduledThreadPoolExecutor executorMock;
    private GameFactory factoryMock;
    private UserConnector connectorMock;
    private GameWorld gameWorldMock;
    private RatingManager ratingManagerMock;


    private void initMocks() {
        initRoomMates();
        executorMock = mock(ScheduledThreadPoolExecutor.class);
        factoryMock = mock(GameFactory.class);
        gameWorldMock = mock(GameWorld.class);
        ratingManagerMock = mock(RatingManager.class);
        when(gameWorldMock.getInit()).thenReturn(mock(JsonObject.class));
        when(gameWorldMock.getUpdate(anyString())).thenReturn(mock(JsonObject.class));
        when(factoryMock.getNewInstance()).thenReturn(gameWorldMock);
        connectorMock = mock(UserConnector.class);
    }

    private void initRoomMates() {
        roomMates = new HashMap<>();
        List<String> room1 = new ArrayList<>();
        room1.add("room1player1");
        room1.add("room1player2");
        room1.add("room1player3");

        List<String> room2 = new ArrayList<>();
        room2.add("room2player1");
        room2.add("room2player2");
        room2.add("room2player3");
        room2.add("room2player4");

        room1.forEach(x->roomMates.put(x, room1));
        room2.forEach(x->roomMates.put(x, room2));
    }

    @Test
    public void testAddPlayer() throws Exception {
        initMocks();
        GameManager gameManager = new GameManager(roomMates, factoryMock, connectorMock, executorMock, ratingManagerMock);

        Session sessionMock = mock(Session.class);
        gameManager.addPlayer("room1player1", sessionMock);
        verify(connectorMock, times(1)).addUser("room1player1", sessionMock);

        gameManager.addPlayer("room1player2", sessionMock);
        verify(connectorMock, times(1)).addUser("room1player2", sessionMock);

        List<String> list = new ArrayList<>();
        list.add("room1player1");
        list.add("room1player2");
        list.add("room1player3");
        when(gameWorldMock.numberOfPlayers()).thenReturn(3);
        when(gameWorldMock.getPlayers()).thenReturn(list);

        gameManager.addPlayer("room1player3", sessionMock);
        verify(connectorMock, times(1)).addUser("room1player3", sessionMock);

        verify(connectorMock, times(1)).sendMessageTo(eq("room1player1"), anyString());
        verify(connectorMock, times(1)).sendMessageTo(eq("room1player2"), anyString());
        verify(connectorMock, times(1)).sendMessageTo(eq("room1player3"), anyString());
    }

    @Test
    public void testRemovePlayer() throws Exception {
        initMocks();
        GameManager gameManager = new GameManager(roomMates, factoryMock, connectorMock, executorMock, ratingManagerMock);
        Session sessionMock1 = mock(Session.class);
        Session sessionMock2 = mock(Session.class);
        Session sessionMock3 = mock(Session.class);

        List<String> list = new ArrayList<>();
        when(gameWorldMock.getPlayers()).thenReturn(list);

        gameManager.removePlayer("room1player1");
        gameManager.removePlayer("room1player2");
        gameManager.removePlayer("room1player3");

        verify(connectorMock).removeUser("room1player1");
        verify(connectorMock).removeUser("room1player2");
        verify(connectorMock).removeUser("room1player3");
    }

    @Test
    public void testSetUpdateFromPlayer() throws Exception {
        initMocks();
        GameManager gameManager = new GameManager(roomMates, factoryMock, connectorMock, executorMock, ratingManagerMock);

        Session sessionMock1 = mock(Session.class);
        Session sessionMock2 = mock(Session.class);
        Session sessionMock3 = mock(Session.class);

        gameManager.addPlayer("room1player1", sessionMock1);
        gameManager.addPlayer("room1player2", sessionMock2);
        gameManager.addPlayer("room1player3", sessionMock3);

        assertTrue(gameManager.setUpdateFromPlayer("room1player1", 2, 2));
        assertTrue(gameManager.setUpdateFromPlayer("room1player2", 3, 4));
        assertTrue(gameManager.setUpdateFromPlayer("room1player1", -2, -7));
        assertFalse(gameManager.setUpdateFromPlayer("wrongName", 0, 0));


        verify(gameWorldMock).setPlayerCoordinates("room1player1", 2, 2);
        verify(gameWorldMock).setPlayerCoordinates("room1player2", 3, 4);
        verify(gameWorldMock).setPlayerCoordinates("room1player1", -2, -7);
        verify(gameWorldMock, never()).setPlayerCoordinates("wrongName", -2, -7);
    }

    @Test
    public void combinedTest1() {
        initMocks();
        List<String> list = mock(ArrayList.class);
        when(list.size()).thenReturn(3);
        when(gameWorldMock.getPlayers()).thenReturn(list);
        when(executorMock.scheduleAtFixedRate(any(), anyInt(), anyInt(), any())).thenReturn(mock(ScheduledFuture.class));

        GameManager gameManager = new GameManager(roomMates, factoryMock, connectorMock, executorMock, ratingManagerMock);
        gameManager.addPlayer("room1player1", mock(Session.class));
        gameManager.addPlayer("room1player2", mock(Session.class));
        gameManager.addPlayer("room1player3", mock(Session.class));
        verify(executorMock, times(4)).scheduleAtFixedRate(any(), anyInt(), anyInt(), any());
    }

    @Test
    public void combinedTest2() {
        initMocks();
        List<String> list = mock(ArrayList.class);
        when(list.size()).thenReturn(1);
        when(gameWorldMock.getPlayers()).thenReturn(list);
        when(executorMock.scheduleAtFixedRate(any(), anyInt(), anyInt(), any())).thenReturn(mock(ScheduledFuture.class));

        GameManager gameManager = new GameManager(roomMates, factoryMock, connectorMock, executorMock, ratingManagerMock);
        gameManager.addPlayer("room1player1", mock(Session.class));
        gameManager.addPlayer("room1player2", mock(Session.class));
        gameManager.addPlayer("room1player3", mock(Session.class));
        verify(executorMock, times(1)).scheduleAtFixedRate(any(), anyInt(), anyInt(), any());
    }
}