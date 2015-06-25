package Core.Model.Dao;

import Core.Model.Bean.Account;
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
                "Values ('"+
                account.getNickname() +"','\n" +
                account.getLastName() +"','\n" +
                account.getFirstName()+"','\n" +
                account.getGender()+"','\n" +
                account.getPassword()+"','\n" +
                account.getBirthDate()+"','\n" +
                account.getAbout()+"','\n" +
                account.getRating()+"')";
        dbWorker.execute(query);
    }

    public void changeUser(iAccount account){
        String query = "UPDATE accounts SET\n" +
                "Nickname = "+ account.getNickname() +",\n" +
                "LastName = "+ account.getLastName() +",\n" +
                "FirstName = "+account.getFirstName()+",\n" +
                "GENDER = "+account.getGender()+",\n" +
                "Password = "+account.getPassword()+",\n" +
                "BirthDate = "+account.getBirthDate()+",\n" +
                "about = "+account.getAbout()+",\n" +
                "GameRating = "+account.getRating()+"\n" +
                "where ID = "+account.getID()+";";
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
      //  result.next();
        account.setNickname(result.getString("Nickname"));
     //   result.next();
        account.setLastName(result.getString("LastName"));
     //   result.next();
        account.setFirstName(result.getString("FirstName"));
     //   result.next();
        account.setGender(result.getString("Gender").equals("Male") ? Account.Gender.MALE : Account.Gender.FEMALE);
      //  result.next();
        account.setPassword(result.getString("password"));
      //  result.next();
      //  account.setBirthDate(result.getDate("birthDate"));
       // result.next();
      //  account.setAbout(result.getString("about"));
       // result.next();
     //   account.setRating(result.getInt("GameRating"));
       // result.next();
     //   account.setMail(result.getString("Mail"));
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
