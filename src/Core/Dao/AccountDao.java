package Core.Dao;

import Core.Bean.Account;
import Interfaces.iAccount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nika on 02:20, 6/25/2015.
 */

public class AccountDao {
    private DBWorker dbWorker;

    public AccountDao(DBWorker dbWorker) {
        this.dbWorker = dbWorker;
    }

    public void registerUser(iAccount account){
        String query = "INSERT INTO accounts \n" +
                "(Nickname, LastName,  FirstName ,  Gender ,  Password ,  BirthDate ,  about ,  GameRating ,  Mail)\n" +
                "Values ('"+account.getNickname()+"'," +
                "'"+account.getFirstName()+"','"+account.getLastName()+
                "','"+account.getGender()+"','"+account.getPassword()+
                "', "+account.getBirthDate()+", '"+account.getAbout()+
                "','"+account.getRating()+"','"+account.getMail()+"')";
        dbWorker.execute(query);
    }

    public void changeUser(iAccount account){
        String query = "UPDATE accounts SET\n" +
                "Nickname = '"+ account.getNickname() +"'," +
                "LastName = '"+ account.getLastName() +"'," +
                "FirstName = '"+account.getFirstName()+"'," +
                "GENDER = '"+account.getGender()+"'," +
                "Password = '"+account.getPassword()+"'," +
                "BirthDate = "+account.getBirthDate()+"," +
                "about = '"+account.getAbout()+"'," +
                "GameRating = '"+account.getRating() +"'\n" +
                "where ID = '"+account.getID()+"';";
        System.out.println(query);
        dbWorker.execute(query);
    }

    public iAccount getUser(String nickname) throws Exception {
        ResultSet result = dbWorker.getResult("SELECT * FROM accounts where Nickname = '" + nickname + "'");

        if (!result.next()) throw new Exception("notRegistered");
        return assembleAccount(result);
    }

    private iAccount assembleAccount(ResultSet result) throws SQLException {
        iAccount account = new Account();
        account.setID(result.getInt("ID"));
        account.setNickname(result.getString("Nickname"));
        account.setLastName(result.getString("LastName"));
        account.setFirstName(result.getString("FirstName"));
        account.setGender(result.getString("Gender").equals("Male") ? Account.Gender.MALE : Account.Gender.FEMALE);
        account.setPassword(result.getString("password"));
        account.setBirthDate(result.getDate("birthDate"));
        account.setAbout(result.getString("about"));
        account.setRating(result.getInt("GameRating"));
        account.setMail(result.getString("Mail"));
        return account;
    }

    public iAccount getUserById(int id) throws SQLException {
        String query = "Select * from accounts where ID = " + id +";";
        ResultSet resultSet = dbWorker.getResult(query);
        return assembleAccount(resultSet);
    }

    public Set<iAccount> getUsersLike(String search){
        Set<iAccount> accounts = new HashSet<>();

        ResultSet result = dbWorker.getResult("select Nickname from Accounts where Nickname like '%" + search + "%'");
        try {
            while (result.next()) {
                accounts.add(getUser(result.getString("Nickname")));
            }
        } catch (Exception e) {
            System.out.println("SQLEXCEPTION");
            return null;
        }
        return accounts;
    }
}
