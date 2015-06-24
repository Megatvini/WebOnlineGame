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
import java.util.Set;

/**
 * Created by gukam on 5/29/2015.
 */
public class UserControl {
   private static Set<Integer> _accounts = new HashSet<Integer>();

    public void addOnlineUser(Integer id){
        _accounts.add(id);
    }

    public void registerUser(iAccount account){
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
        result.close();
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
        account.setID(result.getInt("ID"));
        account.setGender(result.getString("Gender").equals("Male") ? Account.Gender.MALE : Account.Gender.FEMALE);
        // TODO: wamosagebia about, surati, rating, birthdate, mail
        return account;
    }

    public  Set<Integer> getOnlineUsers(){
        Set<Integer> accounts = new HashSet<Integer>();
        accounts.addAll(_accounts);
        return  accounts;
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

    public void addFriend(int idFrom, int idTo){
        String query = "insert into waitingfriends (accIDFrom, AccIDTo) values ("+idFrom+", "+idTo+")";
        (new DBWorker()).execute(query);
    }

    public void confirmFriendRequest(int idFrom, int idTo){
        DBWorker dbWorker = new DBWorker();

        String query = "delete FROM mydb.waitingfriends where accIDTo = " + idFrom +" and accIDFrom = " + idTo +";";
        dbWorker.execute(query);

        query = "insert into friends (accIDFrom, AccIDTo) values ("+idFrom+", "+idTo+")";
        dbWorker.execute(query);

        query = "insert into conversations (AccIDFrom, AccIDTo) values ("+idFrom+", "+idTo+")";
        dbWorker.execute(query);

        query = "insert into conversations (AccIDFrom, AccIDTo) values  ("+idTo+", "+idFrom+")";
        dbWorker.execute(query);
    }

    public int getID(String nickname) throws SQLException {
        ResultSet result = (new DBWorker()).getResult("select ID from accounts where Nickname = '" + nickname +"'");
        result.next();
        return result.getInt("ID");
    }

    public Set<Integer> getWaitingFriends(int id) throws SQLException {
        Set<Integer> waitingFriends = new HashSet<Integer>();

        ResultSet result = (new DBWorker()).getResult("select * from waitingfriends where AccIDTo = " + id);

        while (result.next()) {
            waitingFriends.add(result.getInt("AccIDFrom"));
        }
        return  waitingFriends;
    }

    public Set<Integer> getFriends(int id) throws SQLException {
        Set<Integer> friends = new HashSet<Integer>();

        ResultSet result = (new DBWorker()).getResult("select * from friends where AccIDTo = " + id);
        while (result.next()) {
            friends.add(result.getInt("AccIDFrom"));
        }

        result = (new DBWorker()).getResult("select * from friends where AccIDFrom = " + id);
        while (result.next()) {
            friends.add(result.getInt("AccIDTo"));
        }

        return  friends;
    }
}
