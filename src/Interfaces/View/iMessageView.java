package Interfaces.View;

/**
 * Created by gukam on 5/28/2015.
 */
public interface iMessageView {


    public Message getMessage(int index);
    public void addMessage(String message, Message.Type type);
    public int size();
    public static class Message{
        public enum Type{SENT, GOTTEN}

        Type _type;
        String _message;
        public Message(String message, Message.Type type){
            _message = message;
            _type = type;
        }
        public String getMessage(){
            return _message;
        }

        public Type getType(){
            return _type;
        }

    }

}
