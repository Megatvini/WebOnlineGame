package Core.Dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Nika on 02:56, 6/25/2015.
 */
public class FriendsDao {
    private DataSource dataSource;

    public FriendsDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * make two accounts friends in database
     * @param id1 ID of first user
     * @param id2 ID of second user
     * @return true if data added successfully
     */
    public boolean addFriend(int id1, int id2){
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO friends" +
                    "(accIDFrom, AccIDTo) VALUES (?, ?)")) {
                pst.setInt(1, id1);
                pst.setInt(2, id2);
                pst.execute();

                pst.setInt(1, id2);
                pst.setInt(2, id1);
                pst.execute();
            } catch (SQLException e) {
                //System.out.println("addFriend id1, id2 " + id1 + ", " + id2 + " failed");
                //e.printStackTrace();
                throw new SQLException();
            }
        } catch (SQLException e) {
            //System.out.println("addFriend id1, id2 " + id1 + ", " + id2 + " failed");
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * make two accounts no longer friends in database
     * @param id1 ID of first user
     * @param id2 ID of second user
     * @return true if data was fount and removed successfully
     */
    public boolean removeFriend(int id1, int id2) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM friends " +
                    "WHERE AccIDFrom = ? AND AccIDTo = ?")) {
                pst.setInt(1, id1);
                pst.setInt(2, id2);
                pst.execute();

                pst.setInt(1, id2);
                pst.setInt(2, id1);
                pst.execute();
            } catch (SQLException e) {
                //System.out.println("removeFriend id1, id2 " + id1 + ", " + id2 + " failed");
                //e.printStackTrace();
                throw new SQLException();
            }
        } catch (SQLException e) {
            //System.out.println("removeFriend id1, id2 " + id1 + ", " + id2 + " failed");
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * adds friend request from one user to another
     * @param requestFromID sender of request
     * @param requestToID receiver of request
     * @return true iff data was successfully added to database
     */
    public boolean addFriendRequest(int requestFromID, int requestToID) {
        try (Connection conn = dataSource.getConnection()) {
            try(PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO waitingfriends " +
                    "(accIDFrom, AccIDTo) VALUES (?, ?)")) {
                pst.setInt(1, requestFromID);
                pst.setInt(2, requestToID);
                pst.execute();
            } catch (SQLException e) {
                //System.out.println("addFriendRequest requestFromID, requestToID "
                //                    + requestFromID + ", " + requestFromID + " failed");
                //e.printStackTrace();
                throw new SQLException();
            }
        } catch (SQLException e) {
            //System.out.println("addFriendRequest requestFromID, requestToID "
            //                    + requestFromID + ", " + requestFromID + " failed");
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * accept friend request
     * @param idFrom accountID who accepts request
     * @param idTo accountID who sent request
     * @return tur iff data added successfully to database
     */
    public boolean confirmFriendRequest(int idFrom, int idTo){
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM waitingFriends " +
                    "WHERE AccIDFrom = ? AND AccIDTo = ?")) {
                pst.setInt(1, idTo);
                pst.setInt(2, idFrom);
                pst.execute();
            } catch (SQLException e) {
                //System.out.println("ConfirmFriendRequest idFrom, idTo " +
                //        idFrom + ", " + idTo + " failed");
                //e.printStackTrace();
                conn.rollback();
                throw new SQLException();
            }

            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO friends " +
                    "(AccIDFrom, AccIDTo) VALUES (?, ?)")) {
                pst.setInt(1, idFrom);
                pst.setInt(2, idTo);
                pst.execute();

                pst.setInt(1, idTo);
                pst.setInt(2, idFrom);
                pst.execute();
            } catch (SQLException e) {
                //System.out.println("ConfirmFriendRequest idFrom, idTo " +
                //        idFrom + ", " + idTo + " failed");
                //e.printStackTrace();
                conn.rollback();
                throw new SQLException();
            }

            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO conversations " +
                    "(AccIDFrom, AccIDTo) VALUES (?, ?)")) {
                pst.setInt(1, idFrom);
                pst.setInt(2, idTo);
                pst.execute();

                pst.setInt(1, idTo);
                pst.setInt(2, idFrom);
                pst.execute();
            } catch (SQLException e) {
                //System.out.println("ConfirmFriendRequest idFrom, idTo " +
                //        idFrom + ", " + idTo + " failed");
                //e.printStackTrace();
                conn.rollback();
                throw new SQLException();
            }
            conn.commit();
        } catch (SQLException e) {
            //System.out.println("ConfirmFriendRequest idFrom, idTo " +
            //        idFrom + ", " + idTo + " failed");
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * deletes friend request from database
     * @param idFrom accountID who rejected request
     * @param idTo accountID who sent request
     * @return tur iff data was removed successfully to database
     */
    public boolean rejectFriendRequest(int idFrom, int idTo){
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM waitingfriends " +
                    "WHERE AccIDTo = ? AND AccIDFrom = ?")) {
                pst.setInt(1, idFrom);
                pst.setInt(2, idTo);
                pst.execute();
            } catch (SQLException e) {
                //System.out.println("ConfirmFriendRequest idFrom, idTo " +
                //        idFrom + ", " + idTo + " failed");
                //e.printStackTrace();
                throw new SQLException();
            }
        } catch (SQLException e) {
            //System.out.println("ConfirmFriendRequest idFrom, idTo " +
            //        idFrom + ", " + idTo + " failed");
            //e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * get players to whom account has sent friend requests
     * @param accountName name of an account who sent requests
     * @return set of accountNames  or null if no accountName was found
     */
    public Set<String> getFriendRequestsFrom(String accountName) {
        Set<String> requestsFrom = new TreeSet<>();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT Nickname FROM " +
                            "(SELECT AccIDTo FROM waitingfriends " +
                            "LEFT JOIN accounts ON accIDFrom = accounts.ID " +
                            "WHERE Nickname = ?) AS pa" +
                    "LEFT JOIN accounts ON AccIDTo = accounts.ID")) {
                pst.setString(1, accountName);

                ResultSet result = pst.executeQuery();
                while (result.next()) {
                    requestsFrom.add(result.getString("Nickname"));
                }
            } catch (SQLException e) {
                //System.out.println("getFriendRequestsFrom with name " + accountName + " failed");
                //e.printStackTrace();
                throw new SQLException();
            }
        } catch (SQLException e) {
            //System.out.println("getFriendRequestsFrom with name " + accountName + " failed");
            //e.printStackTrace();
            return null;
        }
        return requestsFrom;
    }

    /**
     * get players to whom account has sent friend requests
     * @param accID ID of an account who sent requests
     * @return set of accountNames or null if accID was not found
     */
    public Set<String> getFriendRequestsFrom(int accID) {
        Set<String> requestsFrom = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            try(PreparedStatement pst = conn.prepareStatement(
                    "SELECT Nickname FROM waitingfriends " +
                    "LEFT JOIN accounts " +
                    "ON AccIDTo = accounts.ID " +
                    "WHERE AccIDFrom = ?")) {
                pst.setInt(1, accID);

                ResultSet result = pst.executeQuery();
                while (result.next()) {
                    requestsFrom.add(result.getString("Nickname"));
                }
            }  catch (SQLException e) {
                //System.out.println("getFriendRequestsFrom with ID" + accID + " failed");
                //e.printStackTrace();
                throw new SQLException();
            }
        } catch (SQLException e) {
            //System.out.println("getFriendRequestsFrom with ID" + accID + " failed");
            //e.printStackTrace();
            return null;
        }
        return requestsFrom;
    }


    /**
     * get players who sent request to account
     * @param accountName name of account to whom friendRequest were sent
     * @return set of request sender names  or null if no accountName found
     */
    public Set<String> getFriendRequestsTo(String accountName) {
        Set<String> requestsTo = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT Nickname FROM " +
                            "(SELECT AccIDFrom FROM waitingfriends " +
                            "LEFT JOIN accounts " +
                            "ON AccIDTo = accounts.ID " +
                            "WHERE Nickname = ?) AS pa " +
                    "LEFT JOIN accounts " +
                    "ON AccIDFrom = accounts.ID")) {
                pst.setString(1, accountName);

                ResultSet result = pst.executeQuery();
                while (result.next()) {
                    requestsTo.add(result.getString("Nickname"));
                }
            } catch (SQLException e) {
                //System.out.println("getFriendRequestsTo with ID" + accID + " failed");
                //e.printStackTrace();
                throw new SQLException();
            }
        } catch (SQLException e) {
            //System.out.println("getFriendRequestsTo with ID" + accID + " failed");
            //e.printStackTrace();
            return null;
        }
        return requestsTo;
    }

    public Set<String> getFriendRequestsTo(int accID) {
        Set<String> requestsTo = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT Nickname FROM waitingfriends " +
                    "LEFT JOIN accounts " +
                    "ON AccIDFrom = accounts.ID " +
                    "WHERE AccIDTo = ?")) {
                pst.setInt(1, accID);

                ResultSet result = pst.executeQuery();
                while (result.next()) {
                    requestsTo.add(result.getString("Nickname"));
                }
            } catch (SQLException e) {
                //System.out.println("getFriendRequestsTo with ID" + accID + " failed");
                //e.printStackTrace();
                throw new SQLException();
            }
        } catch (SQLException e) {
            //System.out.println("getFriendRequestsTo with ID" + accID + " failed");
            //e.printStackTrace();
            return null;
        }
        return requestsTo;
    }

    /**
     * get all friends of account named accountName
     * @param accountName name of the account
     * @return friends of account or null if not found
     */
    public Set<String> getFriendNames(String accountName) {
        Set<String> friends = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT Nickname FROM " +
                            "(SELECT AccIDTo FROM friends " +
                            "LEFT JOIN accounts " +
                            "ON AccIDFrom = accounts.ID " +
                            "WHERE Nickname = ?) AS PA " +
                    "LEFT JOIN accounts " +
                    "ON AccIDTo = accounts.ID")) {
                pst.setString(1, accountName);

                ResultSet result = pst.executeQuery();
                while (result.next()) {
                    friends.add(result.getString("Nickname"));
                }
            }  catch (SQLException e) {
                //System.out.println("getFieldNames with name"+ accountName + " failed");
                //e.printStackTrace();
                throw new SQLException();
            }
        } catch (SQLException e) {
            //System.out.println("getFieldNames with name"+ accountName + " failed");
            //e.printStackTrace();
            return null;
        }
        return friends;
    }

    /**
     * get all friends of account with ID
     * @param id of the account
     * @return friends of account or null if not found
     */
    public Set<String> getFriendNamesByID(int id) {
        Set<String> friends = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT Nickname FROM friends " +
                     "LEFT JOIN accounts " +
                     "ON AccIDTo = accounts.ID " +
                     "WHERE AccIDFrom = ?")) {
                pst.setInt(1, id);

                ResultSet result = pst.executeQuery();
                while (result.next()) {
                    friends.add(result.getString("Nickname"));
                }
            }  catch (SQLException e) {
                //System.out.println("getFieldNames with ID" + id + " failed");
                //e.printStackTrace();
                throw new SQLException();
            }
        } catch (SQLException e) {
            //System.out.println("getFieldNames with ID" + id + " failed");
            //e.printStackTrace();
            return null;
        }
        return friends;
    }
}