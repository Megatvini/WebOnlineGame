package Core.Dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nika on 02:56, 6/25/2015.
 */
public class FriendsDao {
    private DataSource dataSource;

    public FriendsDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public boolean addFriend(int id1, int id2){
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement("INSERT INTO friends (accIDFrom, AccIDTo) VALUES (?, ?)");
            pst.setInt(1, id1);
            pst.setInt(2, id2);
            pst.execute();

            pst.setInt(1, id2);
            pst.setInt(2, id1);
            pst.execute();

            pst.close();
            conn.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean removeFriend(int id1, int id2) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement("DELETE FROM friends WHERE AccIDFrom = ? AND AccIDTo = ?");
            pst.setInt(1, id1);
            pst.setInt(2, id2);
            pst.execute();

            pst.setInt(1, id2);
            pst.setInt(2, id1);
            pst.execute();

            pst.close();
            conn.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean addFriendRequest(int requestFromID, int requestToID) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement("INSERT INTO waitingFriends (accIDFrom, AccIDTo) VALUES (?, ?)");
            pst.setInt(1, requestFromID);
            pst.setInt(2, requestToID);
            pst.execute();

            pst.close();
            conn.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean confirmFriendRequest(int idFrom, int idTo){
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement("DELETE FROM waitingFriends WHERE AccIDTo = ? AND AccIDTo = ?");
            pst.setInt(1, idFrom);
            pst.setInt(2, idTo);
            pst.execute();
            pst.close();

            pst = conn.prepareStatement("INSERT INTO friends (AccIDFrom, AccIDTo) VALUES (?, ?)");
            pst.setInt(1, idFrom);
            pst.setInt(2, idTo);
            pst.execute();

            pst.setInt(2, idFrom);
            pst.setInt(1, idTo);
            pst.execute();

            pst.close();
            conn.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
//        query = "insert into conversations (AccIDFrom, AccIDTo) values ("+idFrom+", "+idTo+")";
//        dbWorker.execute(query);
//
//        query = "insert into conversations (AccIDFrom, AccIDTo) values  ("+idTo+", "+idFrom+")";
//        dbWorker.execute(query);
    }


    public Set<String> getFriendRequestsFrom(String accountName) {
        Set<String> requestsFrom = new HashSet<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT Nickname FROM " +
                        "(SELECT AccIDTo FROM waitingfriends " +
                        "LEFT JOIN accounts ON accIDFrom = accounts.ID " +
                        "WHERE Nickname = ?) AS pa" +
                    "LEFT JOIN accounts ON AccIDTo = accounts.ID");
            pst.setString(1, accountName);

            ResultSet result = pst.executeQuery();
            while (result.next()) {
                requestsFrom.add(result.getString("Nickname"));
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return requestsFrom;
    }

    public Set<String> getFriendRequestsFrom(int accID) {
        Set<String> requestsFrom = new HashSet<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                            "SELECT Nickname FROM waitingfriends " +
                            "LEFT JOIN accounts " +
                            "ON AccIDTo = accounts.ID " +
                            "WHERE AccIDFrom = ?");
            pst.setInt(1, accID);

            ResultSet result = pst.executeQuery();
            while (result.next()) {
                requestsFrom.add(result.getString("Nickname"));
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return requestsFrom;
    }

    public Set<String> getFriendRequestsTo(String accountName) {
        Set<String> requestsTo = new HashSet<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT Nickname FROM " +
                            "(SELECT AccIDFrom FROM waitingfriends " +
                            "LEFT JOIN accounts " +
                            "ON AccIDTo = accounts.ID " +
                            "WHERE Nickname = ?) AS pa " +
                    "LEFT JOIN accounts " +
                    "ON AccIDFrom = accounts.ID");
            pst.setString(1, accountName);

            ResultSet result = pst.executeQuery();
            while (result.next()) {
                requestsTo.add(result.getString("Nickname"));
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return requestsTo;
    }

    public Set<String> getFriendRequestsTo(int accID) {
        Set<String> requestsTo = new HashSet<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT Nickname FROM waitingfriends " +
                    "LEFT JOIN accounts " +
                    "ON AccIDFrom = accounts.ID " +
                    "WHERE AccIDTo = ?");
            pst.setInt(1, accID);

            ResultSet result = pst.executeQuery();
            while (result.next()) {
                requestsTo.add(result.getString("Nickname"));
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return requestsTo;
    }

    public Set<String> getFriendNames(String accountName) {
        Set<String> friends = new HashSet<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT Nickname FROM " +
                            "(SELECT AccIDTo FROM friends " +
                            "LEFT JOIN accounts " +
                            "ON AccIDFrom = accounts.ID " +
                            "WHERE Nickname = ?) AS PA " +
                    "LEFT JOIN accounts " +
                    "ON AccIDTo = accounts.ID");
            pst.setString(1, accountName);

            ResultSet result = pst.executeQuery();
            while (result.next()) {
                friends.add(result.getString("Nickname"));
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return friends;
    }

    public Set<String> getFriendNamesByID(int id) {
        Set<String> friends = new HashSet<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                        "SELECT Nickname FROM friends " +
                        "LEFT JOIN accounts " +
                        "ON AccIDTo = accounts.ID " +
                        "WHERE AccIDFrom = ?");
            pst.setInt(1, id);

            ResultSet result = pst.executeQuery();
            while (result.next()) {
                friends.add(result.getString("Nickname"));
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return friends;
    }
}