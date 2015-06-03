package Core.Controller;

import Core.Model.UserControl;
import Interfaces.Controller.iAccount;
import Interfaces.View.iProfile;
import Interfaces.View.iShorProfile;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * Created by gukam on 5/29/2015.
 */
public class Account implements iAccount {
    Hashtable<String, iShorProfile> _friends = new  Hashtable<String, iShorProfile>();

    @Override
    public void addFriend(String nickname) throws Exception {
        if(nickname.equals(_nickname)) throw new Exception("shen tavs amateb");
        Account friend = new Account(nickname);
        _friends.put(nickname, friend);
    }

    private void addFriend(Hashtable<String, iShorProfile> friends) {
        _friends = friends;
    }

    public Account(String nickname) throws Exception {
       iProfile prof = UserControl.getUser(nickname);

        _nickname = prof.getNickname();
        _fisrtname = prof.getFirstname();
        _lastname = prof.getLastname();
        _gender = prof.getGender();
        _mail = prof.getMail();
        _picPath = prof.getPicturePath();
        _rank = prof.getRank();
        _password = prof.getPassword();
        addFriend(prof.getFriends());
    }

    public Account(){

    }


    private String _nickname = "";
    @Override
    public String getNickname() {

        return _nickname;
    }
    @Override
    public void setNickname(String nickname) {
        _nickname=nickname;
    }

    private String _password = "";
    @Override
    public void setPassword(String password) {
        _password = password;
    }
    @Override
    public String getPassword() {
        return _password;
    }


    private String _picPath= "http://a3.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTIwNjA4NjM0MjAzODMzODY4.jpg";
    @Override
    public String getPicturePath() {
        return _picPath;
    }
    @Override
    public void setPicturePath(String PicPath) {
        _picPath=PicPath;
    }


    private String _fisrtname= "";
    @Override
    public String getFirstname() {
        return _fisrtname;
    }
    @Override
    public void setFirstname(String fisrtname) {
        _fisrtname=fisrtname;
    }


    private String _lastname= "";
    @Override
    public String getLastname() {

        return _lastname= _lastname;
    }
    @Override
    public void setLastname(String lastname) {
        _lastname=lastname;
    }

    private String _mail= "";
    @Override
    public String getMail() {

        return _mail;
    }
    @Override
    public void setMail(String mail) {
        _mail=mail;
    }

    private Gender _gender = Gender.MALE;
    @Override
    public Gender getGender() {

        return _gender;
    }
    @Override
    public void setGender(Gender gender) {
        _gender=gender;
    }

    private int _rank= 0;
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
    public Hashtable<String, iShorProfile> getFriends() {

        return _friends;
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
