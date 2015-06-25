package Interfaces;

import java.util.Date;

/**
 * Created by gukam on 5/28/2015.
 */

public interface iAccount extends iProfile {
    public void setNickname(String nickname);

    public void setPassword(String password);

    public void setPicturePath(String PicPath);

    public void setFirstName(String firstName);

    public void setLastName(String lastName);

    public void setMail(String mail);

    public void setGender(Gender gender);

    public void setRating(int rank);

    public void setID(int ID);

    public void setBirthDate(Date date);

    public String setAbout(String text);
}
