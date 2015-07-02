package MatchMaking;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Nika on 05:02, 6/11/2015.
 */
public class StartingGroup {
    private String creator;
    private Collection<String> group;
    private boolean gameStarted;

    /**
     * this is simple set but
     * one of its elements is
     * unique
     * @param creator name of unique element
     */
    public StartingGroup(String creator) {
        this.creator = creator;
        group = new HashSet<>();
        group.add(creator);
        gameStarted = false;
    }

    /**
     *
     * @return name of player who created this group
     */
    public String getCreator() {
        return creator;
    }

    /**
     *
     * @return the whole group
     */
    public Collection<String> getGroup() {
        return group;
    }


    /**
     * add new uer to the group
     * @param userName name of a user
     */
    public void addUser(String userName) {
        group.add(userName);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
}
