package Core.Model;

import Core.Controller.Account;
import Core.Controller.Message;
import Interfaces.Controller.iAccount;
import Interfaces.View.iProfile;
import Interfaces.View.iShorProfile;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by gukam on 5/29/2015.
 */
public class UserControl {
   private static HashMap<String ,iAccount> _accounts = new HashMap<String, iAccount>();

    public void registerUser(iAccount account){
        _accounts.put(account.getNickname(), account);

        String query = "INSERT INTO accounts " +
                "(Nickname, " +
                "LastName, " +
                "FirstName, " +
                "Gender, " +
                "Password, " +
                "BirthDate, " +
                "About, " +
                "GameRating) " +
                "VALUES " +
                "(" +
                "'"+ account.getNickname() +"', " +
                "'"+account.getLastname()  +"', " +
                "'"+account.getFirstname() +"', " +
                "'"+account.getGender() +"', " +
                "'"+account.getPassword() +"', " +
                "'2015-01-01', " +
                "' ', " +
                "0" +
                ")";

        Integer i = (new DBWorker()).execute(query);
    }

    public void changeUser(int ID, iAccount account){
        _accounts.put(account.getNickname(), account);

        String query = "INSERT INTO accounts " +
                "(Nickname, " +
                "LastName, " +
                "FirstName, " +
                "Gender, " +
                "Password, " +
                "BirthDate, " +
                "About, " +
                "GameRating) " +
                "VALUES " +
                "(" +
                "'"+ account.getNickname() +"', " +
                "'"+account.getLastname()  +"', " +
                "'"+account.getFirstname() +"', " +
                "'"+account.getGender() +"', " +
                "'"+account.getPassword() +"', " +
                "'2015-01-01', " +
                "' ', " +
                "0" +
                ")";

        Integer i = (new DBWorker()).execute(query);
    }

    public void changeUser(String nickname, iAccount account){
        _accounts.put(account.getNickname(), account);

        String query = "INSERT INTO accounts " +
                "(Nickname, " +
                "LastName, " +
                "FirstName, " +
                "Gender, " +
                "Password, " +
                "BirthDate, " +
                "About, " +
                "GameRating) " +
                "VALUES " +
                "(" +
                "'"+ account.getNickname() +"', " +
                "'"+account.getLastname()  +"', " +
                "'"+account.getFirstname() +"', " +
                "'"+account.getGender() +"', " +
                "'"+account.getPassword() +"', " +
                "'2015-01-01', " +
                "' ', " +
                "0" +
                ")";

        Integer i = (new DBWorker()).execute(query);
    }
    public iAccount getUser(String nickname) throws Exception {
        iAccount account = new Account();
        ResultSet result = (new DBWorker()).getResult("SELECT * FROM accounts where Nickname = '" + nickname + "'");
        if (!result.next()) throw new Exception("notRegistered");
        account.setNickname(nickname);
        account.setFirstname(result.getString("FirstName"));
        account.setLastname(result.getString("LastName"));
        account.setPassword(result.getString("Password"));
        account.setGender(result.getString("Gender").equals("Male") ? Account.Gender.MALE : Account.Gender.FEMALE);
        // TODO: wamosagebia about, surati, rating, birthdate, mail
        return account;
    }

    public iAccount getUser(int id) throws Exception {
        iAccount account = new Account();
        ResultSet result = (new DBWorker()).getResult("SELECT * FROM accounts where ID = '" + id + "'");
        if (!result.next()) throw new Exception("notRegistered");
        account.setNickname(result.getString("Nickname"));
        account.setFirstname(result.getString("FirstName"));
        account.setLastname(result.getString("LastName"));
        account.setPassword(result.getString("Password"));
        account.setGender(result.getString("Gender").equals("Male") ? Account.Gender.MALE : Account.Gender.FEMALE);
        // TODO: wamosagebia about, surati, rating, birthdate, mail
        return account;
    }

    public  HashMap<String, iShorProfile> getOnlineUsers(){
        HashMap<String, iShorProfile> users = new HashMap<String, iShorProfile>();
        for(iAccount acc : _accounts.values()){
            users.put(acc.getNickname(), acc);
        }
        return  users;
    }

    public void sendMessage(Message message){

    }
}
