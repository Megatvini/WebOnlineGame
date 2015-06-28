package Core.Dao;

import Core.Bean.Message;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Created by Nika on 18:42, 6/27/2015.
 */

public class CachedMessagesDao {
    private DataSource dataSource;

    public CachedMessagesDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * store messages into database
     * @param accID account who is receiver of messages
     * @param messageMap map of accountName who send message -> messages sent from
     * @return true iff data was added successfully
     */
    public boolean addMessages(int accID, Map<String, List<Message>> messageMap) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO unreadmessages " +
                    "(ReceiverID, SenderNickname, Text, Date)" +
                    " VALUES (?, ?, ?, ?)")) {
                for (String senderNickname : messageMap.keySet()) {
                    List<Message> messages = messageMap.get(senderNickname);
                    for (Message mes : messages) {
                        pst.setInt(1, accID);
                        pst.setString(2, senderNickname);
                        pst.setString(3, mes.getText());
                        pst.setTimestamp(4, new java.sql.Timestamp(mes.getDate().getTime()));
                        pst.execute();
                    }
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                //System.out.println("addMessages with accID" +
                //        accID + " size " + messageMap.size() + " failed");
                throw new SQLException();
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            //System.out.println("addMessages with accID" +
            //        accID + " size " + messageMap.size() + " failed");
            return false;
        }
        return true;
    }

    /**
     * adds single message to database
     * @param accID account it who must receive the message
     * @param senderNickName name of sender account
     * @param text of the message
     * @param date when message was sent
     * @return true if data was added successfully
     */
    public boolean addSingleMessage(int accID, String senderNickName, String text, Date date) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO unreadmessages " +
                    "(ReceiverID, SenderNickname, Text, Date)" +
                    "VALUES (?, ?, ?, ?)")) {
                pst.setInt(1, accID);
                pst.setString(2, senderNickName);
                pst.setString(3, text);
                pst.setTimestamp(4, new java.sql.Timestamp(date.getTime()));
                pst.execute();
            } catch (SQLException e) {
                //e.printStackTrace();
                //System.out.println("addSingleMessage accID, senderNickname, text, date "
                //        + accID + ", " + senderNickName + ", " + text + ", " + date + "failed");
                throw new SQLException();
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            //System.out.println("addSingleMessage accID, senderNickname, text, date "
            //        + accID + ", " + senderNickName + ", " + text + ", " + date + "failed");
            return false;
        }
        return true;
    }

    /**
     * reads and deletes cached messages from database
     * @param accID id of the account whose messages will be read
     * @return map of sender -> list of messages sent
     */
    public Map<String, List<Message>> takeMessages(int accID) {
        Map<String, List<Message>> res = new HashMap<>();
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT * FROM unreadmessages " +
                    "WHERE ReceiverID = ?")) {
                pst.setInt(1, accID);
                ResultSet resultSet = pst.executeQuery();
                assembleResult(res, resultSet);
            } catch (SQLException e) {
                //e.printStackTrace();
                //System.out.println("takeMessages with accID " + accID + " failed");
                conn.rollback();
                throw new SQLException();
            }

            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM unreadmessages " +
                    "WHERE ReceiverID = ?")) {
                pst.setInt(1, accID);
                pst.execute();
            } catch (SQLException e) {
                //e.printStackTrace();
                //System.out.println("removeMessages with accID " + accID + " failed");
                conn.rollback();
                throw new SQLException();
            }
            conn.commit();
        } catch (SQLException e) {
            //e.printStackTrace();
            //System.out.println("takeMessages with accID " + accID + " failed");
            return null;
        }
        return res;
    }


    /**
     * take maps and puts info to it from resultSet
     * @param map map of sender -> list sent messages
     * @param resultSet set of result from mysql database
     * @throws SQLException if resultSet throws
     */
    private void assembleResult(Map<String, List<Message>> map, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String senderName = resultSet.getString("SenderNickname");
            String text = resultSet.getString("Text");
            Date date = resultSet.getTimestamp("Date");
            Message message = new Message();
            message.setDate(date);
            message.setText(text);

            if (map.containsKey(senderName)) {
                map.get(senderName).add(message);
            } else {
                List<Message> list = new ArrayList<>();
                list.add(message);
                map.put(senderName, list);
            }
        }
    }
}
