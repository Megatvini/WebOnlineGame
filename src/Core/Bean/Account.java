package Core.Bean;

import Core.Controller.GameManager;
import Interfaces.iAccount;

import java.util.Date;

/**
 * Created by gukam on 5/29/2015.
 */
public class Account implements iAccount {
    private String nickname;
    private String password;
    private String picPath;
    private String firstName;
    private String lastName;
    private String mail;
    private int rating;
    private int ID;
    private Date birthDate;
    private String about;
    private Gender gender;


    public Account() {
        picPath = "http://a3.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTIwNjA4NjM0MjAzODMzODY4.jpg";
        firstName = "";
        lastName = "";
        about = "";
        rating = GameManager.DEFAULT_RATING;
        gender = Gender.MALE;
        birthDate = new Date(0);
    }

    @Override
    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String getNickname() {
        return nickname;
    }
    @Override
    public void setNickname(String nickname) {
        this.nickname=nickname;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getPicturePath() {
        return picPath;
    }

    @Override
    public void setPicturePath(String PicPath) {
        this.picPath=PicPath;
    }


    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName =firstName;
    }


    @Override
    public String getLastName() {
        return lastName;
    }
    @Override
    public void setLastName(String lastName) {
        this.lastName =lastName;
    }


    @Override
    public String getMail() {
        return mail;
    }
    @Override
    public void setMail(String mail) {
        this.mail=mail;
    }

    @Override
    public Gender getGender() {
        return gender;
    }
    @Override
    public void setGender(Gender gender) {
        this.gender=gender;
    }


    @Override
    public int getRating() {
        return rating;
    }

    @Override
    public void setRating(int rating) {
        this.rating=rating;
    }

    @Override
    public int getID() {
        return ID;
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
    public void setAbout(String text) {
        about = text;
    }

    @Override
    public void setBirthDate(Date date) {
        birthDate = date;
    }

}
