package Interfaces.Controller;
import Interfaces.View.iProfile;

/**
 * Created by gukam on 5/28/2015.
 */
public interface iAccount extends iProfile {
    public void setNickname(String nickname);

    public void setPicturePath(String PicPath);

    public void setFirstname(String fisrtname);

    public void setLastname(String lastname);

    public void setMail(String mail);

    public void setGender(Gender gender);

    public void setRank(int rank);

    public void save();
}
