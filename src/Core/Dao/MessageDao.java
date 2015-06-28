package Core.Dao;

import Core.Bean.Message;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nika on 02:20, 6/25/2015.
 */
public class MessageDao {
    private DataSource dataSource;

    public MessageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean sendMessage(Message message) throws Exception {
        try (Connection connection = dataSource.getConnection();){
            PreparedStatement pst = connection.prepareStatement(
                    "SELECT * FROM conversations " +
                    "WHERE AccIDFrom = ? AND AccIDTo = ?");
            pst.setInt(1, message.getAccFrom());
            pst.setInt(2, message.getAccTo());

            ResultSet result = pst.executeQuery();
            if (!result.next()) throw new Exception("shecdomaaa");
            int conversationIDFrom = result.getInt("ID");

            pst.setInt(1, message.getAccTo());
            pst.setInt(2, message.getAccFrom());

            result = pst.executeQuery();
            if (!result.next()) throw new Exception("shecdomaaa");
            int conversationIDTo = result.getInt("ID");

            boolean b = message.getType().equals(Message.Type.SENT);
            pst.close();

            pst = connection.prepareStatement(
                    "INSERT INTO messages (Text, Sender, Conversations_ID, Date) " +
                    "VALUES (?, ?, ?, ?)");
            pst.setString(1, message.getText());
            pst.setInt(2, b? 1:2);
            pst.setInt(3, conversationIDFrom);
            pst.setDate(4, new Date(message.getDate().getTime()));
            pst.execute();

            pst.setString(1, message.getText());
            pst.setInt(2, b? 2:1);
            pst.setInt(3, conversationIDTo);
            pst.setDate(4, new Date(message.getDate().getTime()));
            pst.execute();

            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Message> getMessages(int userID, int friendID) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT * FROM messages " +
                    "INNER JOIN conversations " +
                    "ON conversations.ID = messages.Conversations_ID " +
                    "WHERE conversations.AccIDFrom = ? AND conversations.AccIDTo = ?");
            pst.setInt(1, userID);
            pst.setInt(2, friendID);

            ResultSet result = pst.executeQuery();
            while (result.next()) {
                Message message = new Message();
                message.setText(result.getString("Text"));
                message.setDate(result.getDate("Date"));
                message.setAccFrom(userID);
                message.setAccTo(friendID);
                message.setType((result.getInt("Sender"))== 1 ? Message.Type.SENT: Message.Type.GOTTEN  );
                messages.add(message);
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return messages;
    }
}
