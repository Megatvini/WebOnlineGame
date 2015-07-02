package Core.Bean;

import Interfaces.iAccount;
import Interfaces.iProfile;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Nika on 03:24, 6/26/2015.
 */
public class AccountTest {

    @Test
    public void testID() throws Exception {
        iAccount account = new Account();
        account.setID(1);
        assertEquals(1, account.getID());
        account.setID(4);
        assertEquals(4, account.getID());
    }

    @Test
    public void testNickname() throws Exception {
        iAccount account = new Account();
        account.setNickname("nika");
        assertTrue(account.getNickname().equals("nika"));
        account.setNickname("guka");
        assertTrue(account.getNickname().equals("guka"));
    }

    @Test
    public void testPassword() throws Exception {
        iAccount account = new Account();
        account.setPassword("sad");
        assertTrue(account.getPassword().equals("sad"));
        account.setPassword("asd");
        assertTrue(account.getPassword().equals("asd"));
    }

    @Test
    public void testPicturePath() throws Exception {
        iAccount account = new Account();
        account.setPicturePath("C:\\programFiles\\nika\\pic.jps");
        assertTrue(account.getPicturePath().equals("C:\\programFiles\\nika\\pic.jps"));
        account.setPicturePath("C:\\programFiles\\nika\\pic2.jps");
        assertTrue(account.getPicturePath().equals("C:\\programFiles\\nika\\pic2.jps"));
    }

    @Test
    public void testFirstName() throws Exception {
        iAccount account = new Account();
        account.setFirstName("nika");
        assertTrue(account.getFirstName().equals("nika"));
        account.setFirstName("guka");
        assertTrue(account.getFirstName().equals("guka"));
    }

    @Test
    public void testLastName() throws Exception {
        iAccount account = new Account();
        account.setLastName("doghonadze");
        assertTrue(account.getLastName().equals("doghonadze"));
        account.setLastName("mazanashvili");
        assertTrue(account.getLastName().equals("mazanashvili"));
    }


    @Test
    public void testMail() throws Exception {
        iAccount account = new Account();
        account.setMail("ndogh13@freeuni.edu.ge");
        assertTrue(account.getMail().equals("ndogh13@freeuni.edu.ge"));
        account.setMail("gmaza13@freeuni.edu.ge");
        assertTrue(account.getMail().equals("gmaza13@freeuni.edu.ge"));
    }

    @Test
    public void testGender() throws Exception {
        iAccount account = new Account();
        account.setGender(iProfile.Gender.FEMALE);
        assertEquals(iProfile.Gender.FEMALE, account.getGender());
        account.setGender(iProfile.Gender.MALE);
        assertEquals(iProfile.Gender.MALE, account.getGender());
    }

    @Test
    public void testRating() throws Exception {
        iAccount account = new Account();
        account.setRating(100);
        assertEquals(100, account.getRating());
        account.setRating(200);
        assertEquals(200, account.getRating());
    }

    @Test
    public void testBirthDate() throws Exception {
        iAccount account = new Account();
        Date date = new Date(System.currentTimeMillis());
        account.setBirthDate(date);
        assertEquals(date, account.getBirthDate());
    }

    @Test
    public void testAbout() throws Exception {
        iAccount account = new Account();
        account.setAbout("someText");
        assertTrue(account.getAbout().equals("someText"));
        account.setAbout("someChangedText");
        assertTrue(account.getAbout().equals("someChangedText"));
    }

    public void combinedTest() {
        iAccount account = new Account();
        account.setID(1654);
        account.setNickname("megatvini");
        account.setFirstName("nika");
        account.setLastName("doghonadze");
        account.setGender(iProfile.Gender.MALE);
        account.setMail("ndogh13@freeuni.edu.ge");
        account.setAbout("text");
        account.setPicturePath("c:\\path\\picture.jpg");
        account.setRating(1554);
        Date date = new Date();
        account.setBirthDate(date);

        assertEquals(1654, account.getID());
        assertTrue(account.getNickname().equals("megatvini"));
        assertTrue(account.getFirstName().equals("nika"));
        assertTrue(account.getLastName().equals("doghonadze"));
        assertEquals(iProfile.Gender.MALE, account.getGender());
        assertTrue(account.getMail().equals("ndogh13@freeuni.edu.ge"));
        assertTrue(account.getAbout().equals("text"));
        assertTrue(account.getPicturePath().equals("c:\\path\\picture.jpg"));
        assertEquals(date, account.getBirthDate());
    }
}