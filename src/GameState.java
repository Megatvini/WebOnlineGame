import javax.websocket.Session;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;


public  class  GameState {
    private Map<Integer,Player> players ;
    private Map<Integer,Session> sessionMap ;
    public Semaphore semaphore  ;
    private Map<Integer,Potion> potions ;
    private final int MAX_POTIONS = 100 ;
    private  final int NULL_POTIONxy = -1000;
    private  final int NULL_POTIONid = -1;


    /**
     * constructor
     */
    public GameState() {
        this.players = new HashMap<>();
        this.semaphore = new Semaphore(1);
        potions = new HashMap<>();
        sessionMap  = new HashMap<>();
    }



    public void deletePotionIfExists(double x , double y ){
        Potion temp  = new Potion(x,y,-1);
        potions.values().stream().filter(potion -> potion.equals(temp)).forEach(potion ->
                potions.put(potion.getId(), new Potion(NULL_POTIONxy,NULL_POTIONxy,potion.getId())));


    }

    public void setPotion(double x , double y ,int i){
        potions.put(i,new Potion(x,y,i));
    }

    public synchronized List<Session>  getSessions() {
        return new ArrayList<>( sessionMap.values());

    }

    public Session sessionById(int id ){
        return sessionMap.get(id);
    }

    public void addSessionById(int id ,  Session session){
        sessionMap.put(id,session);
    }
    public synchronized Player getPlayerById(int playerId){
        if (playerId<players.size())
            return players.get(playerId);
        else return null;
    }

    /*public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }*/

    public synchronized void setPlayerById(int playerId,double x, double y){

        if (playerId>=players.size()){
            addPlayer(playerId, x, y);
        }
        if (players.get(playerId)!=null) {
            players.get(playerId).setX(x);
            players.get(playerId).setY(y);
        }

    }

    private synchronized void addPlayer(int playerId, double x, double y) {
        players.put(playerId,new Player(playerId,x,y));
    }

    public int getPlayerNum(){
        return players.size();
    }

    public int getMAX_POTIONS() {
        return MAX_POTIONS;
    }

    @Override
    public String toString() {
        String toSend = "";
        for (int i = 0 ; i  <players.size(); i ++ ){
            Player p = players.get(i);
            toSend+=p.getId()+":"+p.getX()+":"+p.getY()+",";
        }
        toSend+="#";

        for (int i = 0 ; i < potions.size();i++){
            Potion p = potions.get(i);
            if (p.getX()!=NULL_POTIONxy) {
                toSend += p.getId() + ":" + p.getX() + ":" + p.getY() + ",";
            }else{
                toSend+=p.getId()+":null,";
            }
        }
        return toSend;
    }
}
