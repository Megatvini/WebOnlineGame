
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint(value="/app", configurator=MyConfig.class)
public class App {
    Map<Session, Player> connectedUsers = Collections.synchronizedMap(new HashMap<Session, Player>());

    @OnOpen
    public void open(Session session) {
        connectedUsers.put(session, new Player(150, 100));
        sendDataToUsers();
        System.out.println("new user has connected, size is " + connectedUsers.size() + " this: " + this);
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        //System.out.println("new message received: " + msg + " " + session);
        int move = Integer.parseInt(msg);
        Player user = connectedUsers.get(session);
        switch (move) {
            case 38:  /* Up arrow was pressed */
                user.setY(user.getY() - 5);
                break;
            case 40:  /* Down arrow was pressed */
                user.setY(user.getY() + 5);
                break;
            case 37:  /* Left arrow was pressed */
                user.setX(user.getX()-5);
                break;
            case 39:  /* Right arrow was pressed */
                user.setX(user.getX()+5);
                break;
        }
        sendDataToUsers();
    }

    @OnClose
    public void onClose(Session session) {
        connectedUsers.remove(session);
        System.out.println("connection closed");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.out.println("OnError");
        t.printStackTrace();
    }

    private void sendDataToUsers() {
        String toSend = "";
        Iterator<Session> it = connectedUsers.keySet().iterator();
        while (it.hasNext()) {
            Session s = it.next();
            toSend+= connectedUsers.get(s).toString() + "#";
        }

        it = connectedUsers.keySet().iterator();
        while (it.hasNext()) {
            Session s = it.next();
            try {
                s.getBasicRemote().sendText(toSend);
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("Could not send text");
            }
        }

        //System.out.println("trying to send: " + toSend);
    }
}
