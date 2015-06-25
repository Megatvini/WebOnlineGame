package Interfaces;

import java.util.Date;
import java.util.Set;

/**
 * Created by gukam on 5/28/2015.
 */
public interface iProfile{
    public enum Gender {
        MALE, FEMALE
    }

    public String getFirstName();

    public String getLastName();

    public String getMail();

    public Gender getGender();

    public String getPassword();

    public Set<String> getFriends();

    public String getNickname();

    public String getPicturePath();

    public int getRating();

    public int getID();

    public Set<String> getWaitingFriends();

    public Date getBirthDate();

    public String getAbout();
}
