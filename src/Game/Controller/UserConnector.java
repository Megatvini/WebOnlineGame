package Game.Controller;

import javax.websocket.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class UserConnector {
    private Map<String, RemoteEndpoint.Basic> users;

    public UserConnector() {
        users = new ConcurrentHashMap<>();
    }

    /**
     * adds new user to connector
     * @param userName name of a new user
     * @param connection of a new user
     * @return true iff user was not already added to the connector
     * if user was already added to the connector in the past
     * just updates its session
     */
    public boolean addUser(String userName, RemoteEndpoint.Basic connection) {
        boolean result = !users.containsKey(userName);
        users.put(userName, connection);
        return result;
    }

    /**
     * removes user from connector
     * @param userName name of the user
     * @return true iff user was successfully removed from connector
     */
    public boolean removeUser(String userName) {
        boolean result = users.containsKey(userName);
        users.remove(userName);
        return result;
    }


    /**
     * sends message to userName
     * @param userName name of the user
     * @param message message to be sent
     * @return true iff message was successfully sent
     */
    public boolean sendMessageTo(String userName, String message) {
        RemoteEndpoint.Basic conn = users.get(userName);
        if (conn == null) return false;
        try {
            conn.sendText(message);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}