import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


public  class  GameState {
    private ArrayList<Session> sessions = new ArrayList<>();
    private ArrayList<Player> players ;
    Semaphore semaphore  ;
    private ArrayList<Potion> potions ;


    public GameState(ArrayList<Player> players) {
        this.players = players;
        this.semaphore = new Semaphore(1);
        potions = new ArrayList<>();
        stream();


    }

    public Potion getPotionById(int id ){
        return potions.get(id);

    }
    public Potion getPotionByCoordinates(double x , double y ){
        Potion potion = new Potion(x,y,-1);
        int p =   potions.indexOf(potion);
        if (p!=-1) {
            Potion potion1 = potions.get(p);
            Potion potion2 = new Potion(potion1.getX(),potion1.getY(),potion1.getId());
            potions.set(p,null);
            return potion2;
        }
        return null;
    }



    private void stream() {
        Thread s = new MyThread();
        s.start();
    }

    public synchronized List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public synchronized Player getPlayerById(int playerId){
        if (playerId<players.size())
            return players.get(playerId);
        else return null;
    }

    public synchronized void setPlayerById(int playerId,double x, double y,Session session){

        if (playerId>=players.size()){
            addPlayer(playerId, x, y,session);
        }
        if (players.get(playerId)!=null) {
            players.get(playerId).setX(x);
            players.get(playerId).setY(y);
        }

    }

    private synchronized void addPlayer(int playerId, double x, double y,Session session) {
        players.add(new Player(playerId,x,y,session));
    }
    private synchronized void removePlayer(int playerId) {
        if (players.get(playerId)!=null)
        {
            players.set(playerId,null);

        }else {
            if (players.size()>playerId)
                System.out.println("player already deleted");
            else {
                System.out.println("invalid player number");
            }
        }

    }

    private  class  MyThread extends  Thread{

        @Override
        public void run(){
            int counter = 0 ;
            while (!this.isInterrupted()) {
                counter++;
                //System.out.println("counter is >>--" + counter);
                ArrayList<Player> players2 = new ArrayList<>(players);
                for (Player  player : players2) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (player != null) {
                        Session session =player.getSession();
                        StringBuilder builder = new StringBuilder();
                        builder.append("update");
                        for (Player player1 : players2) {
                            int id = player1.getId();
                            if (id!=player.getId()) {
                                double x = player1.getX();
                                double y = player1.getY();
                                builder.append(",").append(id).append(":").append(x).append(":").append(y);
                            }
                        }
                        try {
                            session.getBasicRemote().sendText(builder.toString());
                            //System.out.println("text was sent "+ builder.toString());

                        } catch (IOException e) {
                            System.out.println("io exception");
                        }
                    }
                    if (counter==100){
                        double x1 =  Helpers.randDouble(10, 400);
                        double y1 =  Helpers.randDouble(10, 400);
                        //System.out.println("counter condition");
                        for (Player p  : players2){
                            Session session = p.getSession();
                            String x = String.format("%.2f", x1);
                            String y = String.format("%.2f", y1);
                            double x2 = Double.parseDouble(x);
                            double y2 = Double.parseDouble(y);
                            int  index = potions.size() ;
                            potions.add(new Potion(x2,y2,index));
                            String toSend  = "itemCreated,"+index+":"+x+":"+y;
                            try {
                                session.getBasicRemote().sendText(toSend);
                                //System.out.println(toSend);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    counter= 0;

                    }
                    semaphore.release();
                }
                try {
                    sleep(66);
                } catch (InterruptedException e) {
                    System.out.println("interrupted exception ");
                }

            }
        }
    }

}
