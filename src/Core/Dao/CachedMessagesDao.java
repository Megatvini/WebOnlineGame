package Core.Dao;

import Core.Bean.Message;
import com.sun.xml.internal.ws.api.model.MEP;

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

    public boolean addMessages(int accID, Map<String, List<Message>> messageMap) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO unreadmessages (ReceiverID, SenderNickname, Text, Date)" +
                    " VALUES (?, ?, ?, ?)");
            for (String senderNickname : messageMap.keySet()) {
                List<Message> messages = messageMap.get(senderNickname);
                for (Message mes : messages) {
                    pst.setInt(1, accID);
                    pst.setString(2, senderNickname);
                    pst.setString(3, mes.getText());
                    pst.setDate(4, new java.sql.Date(mes.getDate().getTime()));
                    pst.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addSingleMessage(int accID, String senderNickName, String text, Date date) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO unreadmessages (ReceiverID, SenderNickname, Text, Date)" +
                    "VALUES (?, ?, ?, ?)");
            pst.setInt(1, accID);
            pst.setString(2, senderNickName);
            pst.setString(3, text);
            pst.setDate(4, new java.sql.Date(date.getTime()));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Map<String, List<Message>> takeMessages(int accID) {
        Map<String, List<Message>> res = new HashMap<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT * FROM unreadmessages WHERE ReceiverID = ?");
            pst.setInt(1, accID);
            ResultSet resultSet = pst.executeQuery();
            assembleResult(res, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    private void assembleResult(Map<String, List<Message>> map, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String senderName = resultSet.getString("SenderNickname");
            String text = resultSet.getString("Text");
            Date date = resultSet.getDate("Date");
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
