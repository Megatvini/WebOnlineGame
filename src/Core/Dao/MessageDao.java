package Core.Dao;

import Core.Bean.Message;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nika on 02:20, 6/25/2015.
 */
public class MessageDao {
    private static final int DEFAULT_MAX_MESSAGES = 50;
    private DataSource dataSource;

    public MessageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * adds message to database
     * @param message to add
     * @return return true if message was successfully added to Database
     */
    public boolean sendMessage(Message message) {
        try (Connection connection = dataSource.getConnection()) {
            int conversationIDFrom;
            int conversationIDTo;
            boolean b;
            connection.setAutoCommit(false);
            try (PreparedStatement pst = connection.prepareStatement(
                    "SELECT * FROM conversations " +
                    "WHERE AccIDFrom = ? AND AccIDTo = ?")) {
                pst.setInt(1, message.getAccFrom());
                pst.setInt(2, message.getAccTo());

                ResultSet result = pst.executeQuery();
                if (!result.next()) throw new SQLException("shecdomaaa");
                conversationIDFrom = result.getInt("ID");

                pst.setInt(1, message.getAccTo());
                pst.setInt(2, message.getAccFrom());

                result = pst.executeQuery();
                if (!result.next()) throw new SQLException("shecdomaaa");
                conversationIDTo = result.getInt("ID");

                b = message.getType().equals(Message.Type.SENT);

            } catch (SQLException e) {
                //System.out.println(e.getErrorCode());
                e.printStackTrace();
                connection.rollback();
                throw new SQLException();
            }

            try (PreparedStatement pst = connection.prepareStatement(
                    "INSERT INTO messages " +
                    "(Text, Sender, Conversations_ID, Date) " +
                    "VALUES (?, ?, ?, ?)")) {
                pst.setString(1, message.getText());
                pst.setInt(2, b? 1:2);
                pst.setInt(3, conversationIDFrom);
                pst.setTimestamp(4, new java.sql.Timestamp(message.getDate().getTime()));
                pst.execute();

                pst.setString(1, message.getText());
                pst.setInt(2, b? 2:1);
                pst.setInt(3, conversationIDTo);
                pst.setTimestamp(4, new java.sql.Timestamp(message.getDate().getTime()));
                pst.execute();

            } catch (SQLException e) {
                //System.out.println(e.getErrorCode());
                e.printStackTrace();
                connection.rollback();
                throw new SQLException();
            }
            connection.commit();
        } catch (SQLException e) {
            //System.out.println(e.getErrorCode());
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * get all messages between user and its friend
     * @param userID ID for a user
     * @param friendID database ID for friend
     * @return list of messages between two users
     */
    public List<Message> getMessages(int userID, int friendID){
        return getMessages(userID, friendID, DEFAULT_MAX_MESSAGES);
    }

    /**
     * get all messages between user and its friend
     * @param userID ID for a user
     * @param friendID database ID for friend
     * @param limit maximum number of limits
     * @return list of messages between two users
     */
    public List<Message> getMessages(int userID, int friendID, int limit){
        ArrayList<Message> messages = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT * FROM messages " +
                    "INNER JOIN conversations " +
                    "ON conversations.ID = messages.Conversations_ID " +
                    "WHERE conversations.AccIDFrom = ? AND conversations.AccIDTo = ? " +
                    "ORDER BY Date DESC " +
                    "LIMIT ?")) {
                pst.setInt(1, userID);
                pst.setInt(2, friendID);
                pst.setInt(3, limit);
                ResultSet result = pst.executeQuery();
                while (result.next()) {
                    Message message = new Message();
                    message.setText(result.getString("Text"));
                    message.setDate(result.getTimestamp("Date"));
                    message.setAccFrom(userID);
                    message.setAccTo(friendID);
                    message.setType((result.getInt("Sender"))== 1 ? Message.Type.SENT: Message.Type.GOTTEN  );
                    messages.add(message);
                }
            } catch (SQLException e) {
                //System.out.println("getMessages userID, friendID " + userID + ", "+ friendID + " failed");
                e.printStackTrace();
                throw new SQLException();
            }
        } catch (SQLException e) {
            //System.out.println("getMessages userID, friendID " + userID + ", "+ friendID + " failed");
            e.printStackTrace();
            return null;
        }
        Collections.reverse(messages);
        return messages;
    }
}
