package Interfaces.Controller;
import Interfaces.View.iProfile;

/**
 * Created by gukam on 5/28/2015.
 */
public interface iAccount extends iProfile {
    public void setNickname();

    public void setPicturePath();

    public void setFirstname();

    public void setLastname();

    public void setMail();

    public void setSex();

    public void save();
}
