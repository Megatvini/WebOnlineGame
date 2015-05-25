
import javax.servlet.ServletContext;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.*;
import javax.websocket.server.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;


@ServerEndpoint(value="/app"
        ,encoders = { SocketIOFrameEncoder.class}
        ,decoders = {SocketIOFrameDecoder.class}
)


public class SocketIOWebSocketHandler  {
    GameState gameState ;
    public SocketIOWebSocketHandler(){
        super();

    }
    @OnOpen
    public void open(Session session) throws IOException, EncodeException {
        System.out.println("opened session >>--" + session.getId());


    }
    @OnClose
    public void close(Session session) throws IOException, EncodeException {
        System.out.println("connection closed >>--" + session.getId());

    }

    @OnMessage
    public void message(Session session, String msg) throws IOException {
        if ("need".equals(msg)){
            gameState = GameStateStatic.getState();
            try {
                gameState.semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ArrayList<Player> players4 = (ArrayList<Player>) gameState.getPlayers();
            int size = players4.size();
            switch (size){
                case 0: gameState.setPlayerById(size,10,10,session); break;
                case 1: gameState.setPlayerById(size,400,10,session); break;
                case 2: gameState.setPlayerById(size,10,400,session); break;
                case 3: gameState.setPlayerById(size,400,400,session); break;
            }


            System.out.println("received need by session :>>-- "+session.getId());
            ArrayList<Player> players = (ArrayList<Player>) gameState.getPlayers();
            StringBuilder builder =new StringBuilder();
            builder.append("initialization");

            for (Player player : players) {
                double x = player.getX();
                double y = player.getY();
                int id = player.getId();
                builder.append(",").append(id).append(":").append(x).append(":").append(y);
            }
            String toSend = builder.toString();
            session.getBasicRemote().sendText(toSend);
          //  System.out.println("sent text as init:"+ builder.toString());
            builder = new StringBuilder();
            builder.append("freshman,");
            Player player2 = gameState.getPlayerById(Integer.parseInt(session.getId()));
            builder.append(player2.getId())
                    .append(":")
                    .append(player2.getX())
                    .append(":")
                    .append(player2.getY());
            for (Player player: gameState.getPlayers()) {
                if (!Objects.equals(player.getId() + "", session.getId())) {
                    player.getSession().getBasicRemote().sendText(builder.toString());
                    //System.out.println("sent text as freshman to " + player.getId() + ":>> " + builder.toString());
                }
            }
            gameState.semaphore.release();
        }else {
            int userId = Integer.parseInt(session.getId());
            updateStateOnServer(userId, msg, session);
        }
    }


    private void updateStateOnServer(int userId, String msg,Session session) {
        StringTokenizer t =new StringTokenizer(msg,",");
        double x = Double.parseDouble(t.nextToken());
        double y = Double.parseDouble(t.nextToken());
        Potion potion = gameState.getPotionByCoordinates(x,y);
        //System.out.println("update");
        if (potion!=null){
            ArrayList<Player> players2 = new ArrayList<>(gameState.getPlayers());
            for (Player player  : players2){
                Session session1 = player.getSession();
                String toSend = potion.getId()+"";
                try {
                    gameState.semaphore.acquire();
                    session1.getBasicRemote().sendText("itemRemoved,"+toSend);

                   // System.out.println("item deleted sent >>--" + toSend);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    gameState.semaphore.release();
                }
            }
        }
        gameState.setPlayerById(userId,x,y,session);
        /*for (int i = 0 ; i < gameState.getPlayers().size();i++){
           // System.out.println(gameState.getPlayerById(i));

        }
*/
    }
}