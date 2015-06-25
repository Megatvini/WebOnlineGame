package Interfaces;


import java.sql.Date;

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

    public String getNickname();

    public String getPicturePath();

    public int getRating();

    public int getID();

    public Date getBirthDate();

    public String getAbout();
}
