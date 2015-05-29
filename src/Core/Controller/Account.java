package Core.Controller;

import Interfaces.Controller.iAccount;
import Interfaces.View.iProfile;
import Interfaces.View.iShorProfile;

import java.util.HashSet;

/**
 * Created by gukam on 5/29/2015.
 */
public class Account implements iAccount {
    public Account(int id){

    }

    @Override
    public void setNickname(String nickname) {

    }

    @Override
    public void setPicturePath(String PicPath) {

    }

    @Override
    public void setFirstname(String fisrtname) {

    }

    @Override
    public void setLastname(String lastname) {

    }

    @Override
    public void setMail(String mail) {

    }

    @Override
    public void setGender(Gender gender) {

    }

    @Override
    public void setRank(int rank) {

    }

    @Override
    public void save() {

    }

    @Override
    public String getFirstname() {
        return "guka";
    }

    @Override
    public String getLastname() {
        return "mazanashvili";
    }

    @Override
    public String getMail() {
        return "guka.mazanashvili@live.com";
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public HashSet<iShorProfile> getFriends() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public String getNickname() {
        return "gukamaz";
    }

    @Override
    public String getPicturePath() {
        return "https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-xtp1/v/t1.0-9/10153796_871894479503609_8567839018459852948_n.jpg?oh=75e5b52126a6b46b3f424e1500d028b8&oe=55F08F06&__gda__=1438776880_f74066263b198b61d991c930e3b7f89b";
    }

    @Override
    public int getRank() {
        return 10;
    }

    @Override
    public int getID() {
        return 1;
    }
}
