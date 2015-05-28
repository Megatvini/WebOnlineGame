package Interfaces.View;

import java.util.HashSet;

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

    public HashSet<iShorProfile> getFriends();

    public boolean isOnline();
}
