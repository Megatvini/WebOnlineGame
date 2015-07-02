package Core.Bean;

import Core.Controller.RatingManager;
import Interfaces.iAccount;
import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Created by gukam on 5/29/2015.
 */
public class Account implements iAccount {
    @Expose
    private String nickname;
    private String password;
    @Expose
    private String picPath ;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    private String mail;
    @Expose
    private int rating;
    private int ID;
    private Date birthDate;
    private String about;
    private Gender gender;


    public Account() {
        picPath = "";
        firstName = "";
        lastName = "";
        about = "";
        rating = RatingManager.DEFAULT_RATING;
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
