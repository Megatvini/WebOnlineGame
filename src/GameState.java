//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.annotations.Expose;
//
//import javax.websocket.Session;
//import java.awt.*;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Semaphore;
//
//
//public  class  GameState {
//    @Expose  private HashMap<Integer,Player>players ;
//
//    private Map<Integer,Session> sessionMap ;
//    public Semaphore semaphore  ;
//    @Expose private HashMap<Integer,Potion> potions ;
//
//
//    /**
//     * constructor
//     */
//    public GameState() {
//        this.players = new HashMap<>();
//        this.semaphore = new Semaphore(1);
//        potions = new HashMap<>();
//        sessionMap  = new HashMap<>();
//    }
//
//
//
//    public void deletePotionIfExists(double x , double y ){
//        Potion temp  = new Potion(x,y,-1);
//        HashMap<Integer,Potion> z = new HashMap<>(potions);
//        for (Potion p : z.values()){
//            if (p.equals(temp)) {
//               // System.out.println(this.toString());
//                potions.put(p.getId(), new Potion(-10000, -10000, p.getId()));
//            }
//        }
//
//
//    }
//
//    public void setPotion(double x , double y ,int i){
//        potions.put(i,new Potion(x,y,i));
//    }
//
//    public synchronized List<Session>  getSessions() {
//        return new ArrayList<>( sessionMap.values());
//
//    }
//
//    public Session sessionById(int id ){
//        return sessionMap.get(id);
//    }
//
//    public void addSessionById(int id ,  Session session){
//        sessionMap.put(id,session);
//    }
//    public synchronized Player getPlayerById(int playerId){
//        if (playerId<players.size())
//            return players.get(playerId);
//        else return null;
//    }
//
//    /*public void setPlayers(ArrayList<Player> players) {
//        this.players = players;
//    }*/
//
//    public synchronized void setPlayerById(int playerId,double x, double y){
//
//        if (playerId>=players.size()){
//            addPlayer(playerId, x, y);
//        }
//        if (players.get(playerId)!=null) {
//            players.get(playerId).setX(x);
//            players.get(playerId).setY(y);
//        }
//
//    }
//
//    private synchronized void addPlayer(int playerId, double x, double y) {
//        players.put(playerId,new Player(playerId,x,y));
//    }
//
//    public int getPlayerNum(){
//        return players.size();
//    }
//
//    public int getMAX_POTIONS() {
//        return 100;
//    }
//
//
//    @Override
//    public String toString() {
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//
//        return gson.toJson(this);
//    }
//}
