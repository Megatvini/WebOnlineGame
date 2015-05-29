package Core.Controller;

import Core.Model.UserControl;
import Interfaces.Controller.iAccount;
import Interfaces.View.iProfile;
import Interfaces.View.iShorProfile;

import java.util.HashSet;

/**
 * Created by gukam on 5/29/2015.
 */
public class Account implements iAccount {
    public Account(String nickname){

    }

    public Account(){

    }


    private String _nickname;
    @Override
    public String getNickname() {

        return _nickname;
    }
    @Override
    public void setNickname(String nickname) {
        _nickname=nickname;
    }


    private String _picPath;
    @Override
    public String getPicturePath() {
        return _picPath;
    }
    @Override
    public void setPicturePath(String PicPath) {
        _picPath=PicPath;
    }


    private String _fisrtname;
    @Override
    public String getFirstname() {
        return _fisrtname;
    }
    @Override
    public void setFirstname(String fisrtname) {
        _fisrtname=fisrtname;
    }


    private String _lastname;
    @Override
    public String getLastname() {

        return _lastname;
    }
    @Override
    public void setLastname(String lastname) {
        _lastname=lastname;
    }

    private String _mail;
    @Override
    public String getMail() {

        return _mail;
    }
    @Override
    public void setMail(String mail) {
        _mail=mail;
    }

    private Gender _gender;
    @Override
    public Gender getGender() {

        return _gender;
    }
    @Override
    public void setGender(Gender gender) {
        _gender=gender;
    }

    private int _rank;
    @Override
    public int getRank() {

        return _rank;
    }
    @Override
    public void setRank(int rank) {
        _rank=rank;
    }

    @Override
    public void save() {
        UserControl.RegisterUser(this);
    }

    @Override
    public HashSet<iShorProfile> getFriends() {

        return null;
    }

    @Override
    public boolean isOnline()
    {
        return true;
    }


    @Override
    public int getID() {

        return 1;
    }
}
