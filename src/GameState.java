import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import javax.websocket.Session;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;


public  class  GameState {
    @Expose  private Player[] players ;

    private Map<Integer,Session> sessionMap ;
    public Semaphore semaphore  ;
    @Expose private Potion [] potions ;


    /**
     * constructor
     */
    public GameState() {
        this.players = new Player[4];
        this.semaphore = new Semaphore(1);
        potions = new Potion[100];
        sessionMap  = new HashMap<>();
    }



    public void deletePotionIfExists(double x , double y ){
        Potion temp  = new Potion(x,y,-1);
        for (int i = 0 ; i < potions.length ;i ++){
            if (temp.equals(potions[i])){
                potions[i]= new Potion(-1000,-1000,-1);
            }

        }


    }

    public void setPotion(double x , double y ,int i){

        potions[i] = new Potion(x,y,i);
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
        if (playerId<players.length)
            return players[playerId];
        else return null;
    }

    /*public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }*/

    public synchronized void setPlayerById(int playerId,double x, double y){

        if (playerId>=players.length){
            addPlayer(playerId, x, y);
        }
        if (players[playerId]!=null) {
            players[playerId].setX(x);
            players[playerId].setY(y);
        }

    }

    private synchronized void addPlayer(int playerId, double x, double y) {
        players[playerId] = new Player(playerId,x,y) ;
    }

    public int getPlayerNum(){
        return players.length;
    }

    public int getMAX_POTIONS() {
        return 100;
    }


    @Override
    public String toString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        return gson.toJson(this);
    }
}
