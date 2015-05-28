package Interfaces.View;

/**
 * Created by gukam on 5/28/2015.
 */
public interface iProfile {
    public enum Gender {
        MALE, FEMALE
    }

    public String getNickname();

    public String getPicturePath();

    public String getFirstname();

    public String getLastname();

    public String getMail();

    public Gender getGender();

    public int getRank();
}
