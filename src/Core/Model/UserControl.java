package Core.Model;

import Core.Controller.Account;
import Core.Controller.Message;
import Interfaces.Controller.iAccount;
import Interfaces.View.iProfile;
import Interfaces.View.iShorProfile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        account.setID(result.getInt("ID"));
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

    public void sendMessage(Message message) throws Exception {

        ResultSet result = (new DBWorker()).getResult("SELECT * FROM conversations where AccIDFrom = " + message.getAccFrom() +
                                                                                    " AND AccIDTo =" + message.getAccTo());

        if (!result.next()) throw new Exception("shecdomaaa");
        int conversationIDFrom = result.getInt("ID");


        result = (new DBWorker()).getResult("SELECT * FROM conversations where AccIDFrom = " + message.getAccTo() +
                " AND AccIDTo =" + message.getAccFrom());

        if (!result.next()) throw new Exception("shecdomaaa");
        int conversationIDTo = result.getInt("ID");

        boolean b = message.getType().equals(Message.Type.SENT);
        String query =  "insert into messages (Text, Sender, Conversations_ID) values ('"+ message.getText() +"', "+ (b? 1:2) +", " + conversationIDFrom + "); " ;
       (new DBWorker()).execute(query);
        query = "insert into messages (Text, Sender, Conversations_ID) values ('"+ message.getText() +"', "+ (b? 2:1) +", " + conversationIDTo + "); ";
        (new DBWorker()).execute(query);
    }

    public ArrayList<Message> getMessages(int userID, int friendID) throws SQLException {

        ArrayList<Message> messages = new ArrayList<Message>();
        ResultSet result = (new DBWorker()).getResult("select * from messages inner join conversations"+
                " on conversations.ID = messages.Conversations_ID where conversations.AccIDFrom = " + userID +
                " and conversations.AccIDTo = " + friendID );

        while (result.next()) {
            Message message = new Message();
            message.setText(result.getString("Text"));
            message.setAccFrom(userID);
            message.setAccTo(friendID);
            message.setType(   (result.getInt("Sender"))== 1 ? Message.Type.SENT: Message.Type.GOTTEN  );
            messages.add(message);
            //TODO date
        }
        return  messages;
    }
}
