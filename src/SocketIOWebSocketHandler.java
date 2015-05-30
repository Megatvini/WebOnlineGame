
import javax.websocket.*;
import javax.websocket.server.*;
import java.io.IOException;
import java.util.*;


@ServerEndpoint(value="/app"
        ,encoders = { SocketIOFrameEncoder.class}
        ,decoders = {SocketIOFrameDecoder.class}
)


public class SocketIOWebSocketHandler  {
    GameState gameState ;
    @OnOpen
    public void open(Session session) throws IOException, EncodeException {
       gameState = GameStateStatic.getState();
        System.out.println("opened session >>--" + session.getId());
        int size =gameState.getPlayerNum();
        try {
            gameState.semaphore.acquire();
            int s  = Integer.parseInt(session.getId());

            session.getBasicRemote().sendText("init#"+s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            gameState.semaphore.release();
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int id = 0  ;
        switch (size){
            case 0: gameState.setPlayerById(size,10,10);
                id = 0 ;
                break;
            case 1: gameState.setPlayerById(size,400,10);
                id = 1  ;
                break;
            case 2: gameState.setPlayerById(size,10,400);
                id = 2  ;
                break;
            case 3: gameState.setPlayerById(size,400,400);
                id = 3  ;
                break;
        }
        gameState.addSessionById(id,session);
        new Streamer(gameState);

    }
    @OnClose
    public void close(Session session) throws IOException, EncodeException {
        System.out.println("connection closed >>--" + session.getId());

    }

    @OnMessage
    public void message(Session session, String msg) throws IOException {

                int userId = Integer.parseInt(session.getId());
                updateStateOnServer(userId, msg);



    }


    private void updateStateOnServer(int userId, String msg) {
        StringTokenizer t =new StringTokenizer(msg,",");
        double x = Double.parseDouble(t.nextToken());
        double y = Double.parseDouble(t.nextToken());
        gameState.setPlayerById(userId,x,y);
        gameState.deletePotionIfExists(x, y);

    }
}