package Core.Model;

import Core.Controller.Account;
import Core.Controller.Message;
import Interfaces.Controller.iAccount;
import Interfaces.View.iProfile;
import Interfaces.View.iShorProfile;

import java.sql.Connection;
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
    DBWorker dbWorker = new DBWorker();

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

        Connection conn = dbWorker.getConnection();
        Integer i = dbWorker.execute(query, conn);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        Connection conn = dbWorker.getConnection();
        Integer i = dbWorker.execute(query, conn);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        Connection conn = dbWorker.getConnection();
        Integer i = dbWorker.execute(query, conn);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public iAccount getUser(String nickname) throws Exception {
        Connection conn = dbWorker.getConnection();
        ResultSet result = dbWorker.getResult("SELECT * FROM accounts where Nickname = '" + nickname + "'", conn);

        if (!result.next()) throw new Exception("notRegistered");

        iAccount account = new Account();
        account.setNickname(nickname);
        account.setID(result.getInt("ID"));
        account.setFirstname(result.getString("FirstName"));
        account.setLastname(result.getString("LastName"));
        account.setPassword(result.getString("Password"));
        account.setGender(result.getString("Gender").equals("Male") ? Account.Gender.MALE : Account.Gender.FEMALE);
        // TODO: wamosagebia about, surati, rating, birthdate, mail
        try {
            result.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
    public iAccount getUser(int id) throws Exception {
        Connection conn = dbWorker.getConnection();
        ResultSet result = dbWorker.getResult("SELECT * FROM accounts where ID = '" + id + "'", conn);

        if (!result.next()) throw new Exception("notRegistered");

        iAccount account = new Account();
        account.setNickname(result.getString("Nickname"));
        account.setFirstname(result.getString("FirstName"));
        account.setLastname(result.getString("LastName"));
        account.setPassword(result.getString("Password"));
        account.setID(result.getInt("ID"));
        account.setGender(result.getString("Gender").equals("Male") ? Account.Gender.MALE : Account.Gender.FEMALE);
        // TODO: wamosagebia about, surati, rating, birthdate, mail

        try {
            result.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return account;
    }

    public  Set<Integer> getOnlineUsers(){
        Set<Integer> accounts = new HashSet<Integer>();
        accounts.addAll(_accounts);
        return  accounts;
    }

    public void sendMessage(Message message) throws Exception {
        Connection conn = dbWorker.getConnection();
        ResultSet result = dbWorker.getResult("SELECT * FROM conversations where AccIDFrom = " + message.getAccFrom() +
                " AND AccIDTo =" + message.getAccTo(), conn);

        if (!result.next()) throw new Exception("shecdomaaa");
        int conversationIDFrom = result.getInt("ID");

        result = dbWorker.getResult("SELECT * FROM conversations where AccIDFrom = " + message.getAccTo() +
                " AND AccIDTo =" + message.getAccFrom(), conn);

        if (!result.next()) throw new Exception("shecdomaaa");
        int conversationIDTo = result.getInt("ID");

        boolean b = message.getType().equals(Message.Type.SENT);
        String query =  "insert into messages (Text, Sender, Conversations_ID) values ('"+ message.getText() +"', "+ (b? 1:2) +", " + conversationIDFrom + "); " ;
        dbWorker.execute(query, conn);
        query = "insert into messages (Text, Sender, Conversations_ID) values ('"+ message.getText() +"', "+ (b? 2:1) +", " + conversationIDTo + "); ";
        dbWorker.execute(query, conn);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Message> getMessages(int userID, int friendID) throws SQLException {

        ArrayList<Message> messages = new ArrayList<Message>();

        Connection conn = dbWorker.getConnection();
        ResultSet result = dbWorker.getResult("select * from messages inner join conversations" +
                " on conversations.ID = messages.Conversations_ID where conversations.AccIDFrom = " + userID +
                " and conversations.AccIDTo = " + friendID, conn);

        while (result.next()) {
            Message message = new Message();
            message.setText(result.getString("Text"));
            message.setAccFrom(userID);
            message.setAccTo(friendID);
            message.setType(   (result.getInt("Sender"))== 1 ? Message.Type.SENT: Message.Type.GOTTEN  );
            messages.add(message);
            //TODO date
        }

        try {
            result.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  messages;
    }

    public void addFriend(int idFrom, int idTo){

        Connection conn = dbWorker.getConnection();
        String query = "insert into waitingfriends (accIDFrom, AccIDTo) values ("+idFrom+", "+idTo+")";
        dbWorker.execute(query, conn);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void confirmFriendRequest(int idFrom, int idTo){
        Connection conn = dbWorker.getConnection();
        String query = "delete FROM mydb.waitingfriends where accIDTo = " + idFrom +" and accIDFrom = " + idTo +";";
        dbWorker.execute(query, conn);

        query = "insert into friends (accIDFrom, AccIDTo) values ("+idFrom+", "+idTo+")";
        dbWorker.execute(query, conn);

        query = "insert into conversations (AccIDFrom, AccIDTo) values ("+idFrom+", "+idTo+")";
        dbWorker.execute(query, conn);

        query = "insert into conversations (AccIDFrom, AccIDTo) values  ("+idTo+", "+idFrom+")";
        dbWorker.execute(query, conn);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getID(String nickname) throws SQLException {
        Connection conn = dbWorker.getConnection();
        ResultSet result = dbWorker.getResult("select ID from accounts where Nickname = '" + nickname + "'", conn);
        result.next();
        int id = result.getInt("ID");

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public Set<Integer> getWaitingFriends(int id) throws SQLException {
        Set<Integer> waitingFriends = new HashSet<Integer>();

        Connection conn = dbWorker.getConnection();
        ResultSet result = dbWorker.getResult("select * from waitingfriends where AccIDTo = " + id, conn);

        while (result.next()) {
            waitingFriends.add(result.getInt("AccIDFrom"));
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  waitingFriends;
    }

    public Set<Integer> getFriends(int id) throws SQLException {
        Set<Integer> friends = new HashSet<Integer>();

        Connection conn = dbWorker.getConnection();
        ResultSet result = dbWorker.getResult("select * from friends where AccIDTo = " + id, conn);
        while (result.next()) {
            friends.add(result.getInt("AccIDFrom"));        }


        result = dbWorker.getResult("select * from friends where AccIDFrom = " + id, conn);
        while (result.next()) {
            friends.add(result.getInt("AccIDTo"));
        }


        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  friends;
    }

    public Set<Integer> getUsersLike(String search) throws SQLException {
        Set<Integer> friends = new HashSet<Integer>();

        Connection conn = dbWorker.getConnection();
        ResultSet result = dbWorker.getResult("select * from Accounts where Nickname like '%" + search + "%'", conn);
        while (result.next()) {
            friends.add(result.getInt("ID"));
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  friends;
    }
}
