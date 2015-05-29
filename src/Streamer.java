import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by rezo on 5/26/15. streaming object
 */
public class Streamer {
    boolean started = false ;

    public Streamer(GameState gameState){
        if (!started){
            new StreamerThread (gameState).start();
           new PotionThread(gameState).start();
            started=true;
            System.out.println("stream started");
        }
    }

    private class  StreamerThread extends Thread{
        GameState gameState ;
        public StreamerThread(GameState gameState){
            this.gameState=gameState;
        }
        @Override
        public void run(){
            while (true) {
                try {
                    sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                oneUpdate();
            }
        }

        private void oneUpdate() {

            String toSend = this.gameState.toString();
            //System.out.println(toSend);
            for (Session session : this.gameState.getSessions()){
                try {
                    this.gameState.semaphore.acquire();
                   // System.out.println(toSend);
                    session.getBasicRemote().sendText(toSend);

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    this.gameState.semaphore.release();
                }
            }
        }
    }

    private class PotionThread extends Thread{
        GameState gameState ;
        public PotionThread (GameState gameState){
            this.gameState=gameState;


        }

        @Override
        public void run(){

            for (int i = 0; i < gameState.getMAX_POTIONS(); i++) {
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String x = String.format("%.2f", Helpers.randDouble(0,400));
                String y = String.format("%.2f", Helpers.randDouble(0,400));
                double d = Double.parseDouble(x);
                double d1  = Double.parseDouble(y);
                gameState.setPotion(d ,d1,i);

            }
        }
    }

}
