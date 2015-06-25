package Core.Model.Bean;

import Interfaces.iAccount;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gukam on 5/29/2015.
 */
public class Account implements iAccount {
    private String _nickname;
    private String _password;
    private String _picPath= "http://a3.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTIwNjA4NjM0MjAzODMzODY4.jpg";
    private String _firstName;
    private String _lastName ;
    private String _mail;
    private int _rating;
    private int _ID;
    private Date birthDate;
    private String about;

    @Override
    public void setID(int ID) {
        _ID = ID;
    }

    @Override
    public String getNickname() {
        return _nickname;
    }
    @Override
    public void setNickname(String nickname) {
        _nickname=nickname;
    }

    @Override
    public void setPassword(String password) {
        _password = password;
    }
    @Override
    public String getPassword() {
        return _password;
    }


    @Override
    public String getPicturePath() {
        return _picPath;
    }

    @Override
    public void setPicturePath(String PicPath) {
        _picPath=PicPath;
    }


    @Override
    public String getFirstName() {
        return _firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        _firstName =firstName;
    }


    @Override
    public String getLastName() {
        return _lastName;
    }
    @Override
    public void setLastName(String lastName) {
        _lastName =lastName;
    }


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


    @Override
    public int getRating() {
        return _rating;
    }

    @Override
    public void setRating(int rating) {
        _rating=rating;
    }

    @Override
    public int getID() {
        return _ID;
    }


    @Override
    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public String getAbout() {
        return about;
    }

    @Override
    public String setAbout(String text) {
        return about = text;
    }

    @Override
    public void setBirthDate(Date date) {
        birthDate = date;
    }

}
