package Core.Dao;

import Core.Bean.Message;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        try {
            Connection connection = dataSource.getConnection();
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
                    "INSERT INTO messages (Text, Sender, Conversations_ID) " +
                    "VALUES (?, ?, ?)");
            pst.setString(1, message.getText());
            pst.setInt(2, b? 1:2);
            pst.setInt(3, conversationIDFrom);
            pst.execute();

            pst.setString(1, message.getText());
            pst.setInt(2, b? 2:1);
            pst.setInt(3, conversationIDTo);
            pst.execute();

            pst.close();
            connection.close();
        } catch (SQLException e) {
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
                message.setAccFrom(userID);
                message.setAccTo(friendID);
                message.setType((result.getInt("Sender"))== 1 ? Message.Type.SENT: Message.Type.GOTTEN  );
                messages.add(message);
                //TODO date
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return messages;
    }
}
