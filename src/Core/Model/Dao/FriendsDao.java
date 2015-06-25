package Core.Model.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nika on 02:56, 6/25/2015.
 */
public class FriendsDao {
    private DBWorker dbWorker;

    public FriendsDao(DBWorker dbWorker) {
        this.dbWorker = dbWorker;
    }


    public void addFriend(int id1, int id2){
        String query = "insert into friends (accIDFrom, AccIDTo) values ("+id1+", "+id2+")";
        dbWorker.execute(query);

        query = "insert into friends (accIDFrom, AccIDTo) values ("+id2+", "+id1+")";
        dbWorker.execute(query);
    }

    public void removeFriend(int id1, int id2) {
        String query = "DELETE From friends\n" +
                "where AccIDFrom = "+id1+" AND AccIDTo = "+id2+";";
        dbWorker.execute(query);

        query = "DELETE From friends\n" +
                "where AccIDFrom = "+id2+" AND AccIDTo = "+id1+";";
        dbWorker.execute(query);
    }

    public void addFriendRequest(int requestFromID, int requestToID) {
        String query = "insert into waitingFriends (accIDFrom, AccIDTo) values ("+requestFromID+", "+requestToID+")";
        dbWorker.execute(query);
    }

    public void confirmFriendRequest(int idFrom, int idTo){
        String query = "delete FROM waitingFriends where accIDTo = " + idFrom +" and accIDFrom = " + idTo +";";
        dbWorker.execute(query);

        query = "insert into friends (accIDFrom, AccIDTo) values ("+idFrom+", "+idTo+")";
        dbWorker.execute(query);

//        query = "insert into conversations (AccIDFrom, AccIDTo) values ("+idFrom+", "+idTo+")";
//        dbWorker.execute(query);
//
//        query = "insert into conversations (AccIDFrom, AccIDTo) values  ("+idTo+", "+idFrom+")";
//        dbWorker.execute(query);
    }

    public Set<String> getFriendRequestsFrom(String accountName) {
        Set<String> requestFrom = new HashSet<>();
        String query = "select Nickname from \n" +
                "\t(select AccIDTo from waitingfriends\n" +
                "\tleft join accounts\n" +
                "\ton AccIDFrom = accounts.ID\n" +
                "\twhere Nickname ="+ accountName +") as pa\n" +
                "left join accounts\n" +
                "on AccIDTo = accounts.ID";

        ResultSet result = dbWorker.getResult(query);
        try {
            while (result.next()) {
                requestFrom.add(result.getString("Nickname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  requestFrom;
    }

    public Set<String> getFriendRequestsFrom(int accID) {
        Set<String> requestFrom = new HashSet<>();
        String query = "select nickName from waitingfriends\n" +
                "left join accounts\n" +
                "on AccIDTo = accounts.ID\n" +
                "where AccIDFrom = "+accID+";";
        ResultSet resultSet = dbWorker.getResult(query);
        try {
            while (resultSet.next()) requestFrom.add(resultSet.getString("nickName"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestFrom;
    }

    public Set<String> getFriendRequestsTo(String accountName) {
        Set<String> requestsTo = new HashSet<>();
        String query = "select Nickname from \n" +
                "\t(select AccIDFrom from waitingfriends\n" +
                "\tleft join accounts\n" +
                "\ton AccIDTo = accounts.ID\n" +
                "\twhere Nickname ="+ accountName +") as pa\n" +
                "left join accounts\n" +
                "on AccIDFrom = accounts.ID";

        ResultSet result = dbWorker.getResult(query);
        try {
            while (result.next()) {
                requestsTo.add(result.getString("Nickname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestsTo;
    }

    public Set<String> getFriendRequestsTo(int accID) {
        Set<String> requestFrom = new HashSet<>();
        String query = "select Nickname from waitingfriends\n" +
                "left join accounts\n" +
                "on AccIDFrom = accounts.ID\n" +
                "where AccIDTo = "+accID+";";
        ResultSet resultSet = dbWorker.getResult(query);
        try {
            while (resultSet.next()) requestFrom.add(resultSet.getString("nickName"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestFrom;
    }

    public Set<String> getFriendNames(String accountName) {
        Set<String> friends = new HashSet<>();
        String query = "select NickName from \n" +
                    "(select AccIDTo from friends\n" +
                    "left join accounts\n" +
                    "on AccIDFrom = accounts.ID\n" +
                    "where Nickname = "+accountName+") as pa\n" +
                "left join accounts\n" +
                "on AccIDTo = accounts.ID;";
        ResultSet result = dbWorker.getResult(query);
        try {
            while (result.next()) friends.add(result.getString("Nickname"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  friends;
    }

    public Set<String> getFriendNamesByID(int id) {
        Set<String> friends = new HashSet<>();
        String query = "select Nickname from friends\n" +
                "left join accounts\n" +
                "on AccIDTo = accounts.ID\n" +
                "where AccIDFrom = "+ id +";";
        ResultSet result = dbWorker.getResult(query);
        try {
            while (result.next()) friends.add(result.getString("Nickname"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  friends;
    }
}