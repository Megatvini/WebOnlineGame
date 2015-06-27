package Core.Bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nika on 16:38, 6/26/2015.
 */
public class Game {
    private Date date;
    private int gameID;
    private int ratingChange;
    private Set<Integer> participantIDs;

    /**
     *
     * @param date when game was Finished
     * @param gameID Id of the game in database
     * @param ratingChange ratingChange of a player who played this game
     */
    public Game(Date date, int gameID, int ratingChange) {
        this.date = date;
        this.gameID = gameID;
        this.ratingChange = ratingChange;
        this.participantIDs = new HashSet<>();
    }

    /**
     *
     * @return date when game was finished
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @param date when game was finished
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     *
     * @return ID of the game in database
     */
    public int getGameID() {
        return gameID;
    }

    /**
     *
     * @param gameID ID of the game in database
     */
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    /**
     *
     * @return ratingChange of a player who played this game
     */
    public int getRatingChange() {
        return ratingChange;
    }

    /**
     *
     * @param ratingChange ratingChange of a player who played this game
     */
    public void setRatingChange(int ratingChange) {
        this.ratingChange = ratingChange;
    }

    /**
     *
     * @param participantID add new participant ID to game
     */
    public void addParticipant(int participantID) {
        participantIDs.add(participantID);
    }

    /**
     *
     * @param participantID removeParticipant ID from game
     */
    public void removeParticipant(int participantID){
        participantIDs.remove(participantID);
    }

    /**
     *
     * @return ID-s if all participants who played game
     */
    public Set<Integer> getParticipantIDs() {
        return participantIDs;
    }
}
