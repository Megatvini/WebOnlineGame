package Core.Bean;

import Interfaces.iAccount;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Nika on 16:09, 6/28/2015.
 */
public class NotificationMessage {
    @Expose
    private iAccount sender;
    @Expose
    private List<Message> messages;

    public NotificationMessage(iAccount sender, List<Message> messages) {
        this.sender = sender;
        this.messages = messages;
    }

    public iAccount getSender() {
        return sender;
    }

    public void setSender(iAccount sender) {
        this.sender = sender;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
