package Interfaces.View;

import Core.View.MessageList;

import java.util.Hashtable;

/**
 * Created by gukam on 5/28/2015.
 */
public interface iShorProfile {
    public String getNickname();

    public String getPicturePath();

    public int getRank();

    public int getID();

    public Hashtable<String, iShorProfile> getWaitingFriends();

    public Hashtable<String, MessageList> getUnreadMessages();
}
