package Core.Dao;

import Core.Bean.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nika on 02:20, 6/25/2015.
 */
public class MessageDao {
    private DBWorker dbWorker;

    public MessageDao(DBWorker dbWorker) {
        this.dbWorker = dbWorker;
    }

    public void sendMessage(Message message) throws Exception {
        ResultSet result = dbWorker.getResult("SELECT * FROM conversations where AccIDFrom = " + message.getAccFrom() +
                " AND AccIDTo =" + message.getAccTo());

        if (!result.next()) throw new Exception("shecdomaaa");
        int conversationIDFrom = result.getInt("ID");

        result = dbWorker.getResult("SELECT * FROM conversations where AccIDFrom = " + message.getAccTo() +
                " AND AccIDTo =" + message.getAccFrom());

        if (!result.next()) throw new Exception("shecdomaaa");
        int conversationIDTo = result.getInt("ID");

        boolean b = message.getType().equals(Message.Type.SENT);
        String query =  "insert into messages (Text, Sender, Conversations_ID) values ('"+ message.getText() +"', "+ (b? 1:2) +", " + conversationIDFrom + "); " ;
        dbWorker.execute(query);
        query = "insert into messages (Text, Sender, Conversations_ID) values ('"+ message.getText() +"', "+ (b? 2:1) +", " + conversationIDTo + "); ";
        dbWorker.execute(query);
    }

    public List<Message> getMessages(int userID, int friendID) throws SQLException {

        ArrayList<Message> messages = new ArrayList<>();

        ResultSet result = dbWorker.getResult("select * from messages inner join conversations" +
                " on conversations.ID = messages.Conversations_ID where conversations.AccIDFrom = " + userID +
                " and conversations.AccIDTo = " + friendID);

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
}
