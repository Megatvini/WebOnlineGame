package Interfaces.View;

import Core.View.MessageList;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * Created by gukam on 5/28/2015.
 */
public interface iProfile extends iShorProfile{
    public enum Gender {
        MALE, FEMALE
    }

    public String getFirstname();

    public String getLastname();

    public String getMail();

    public Gender getGender();

    public String getPassword();

    public Hashtable<String, iShorProfile> getFriends();

    public Hashtable<String, iShorProfile> getWaitingFriends();

    public MessageList getMessages(String nickname);

    public boolean isOnline();
}
