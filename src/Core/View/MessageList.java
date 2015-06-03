package Core.View;

import Interfaces.View.iMessageView;

import java.util.Vector;

/**
 * Created by gukam on 6/3/2015.
 */
public class MessageList implements iMessageView {
    Vector<Message> _messages = new Vector<Message>();

    @Override
    public Message getMessage(int index) {
        return _messages.get(index);
    }

    @Override
    public void addMessage(String message, Message.Type type) {
        _messages.add(new Message(message, type));
    }

    @Override
    public int size() {
        return _messages.size();
    }
}
