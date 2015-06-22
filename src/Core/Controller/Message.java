package Core.Controller;

import java.util.Date;

/**
 * Created by Annie on 22-Jun-15.
 */
public class Message {
    public enum Type{SENT, GOTTEN}
    public Message(String accFrom,String accTo, String text, Message.Type type, Date date){
        _accFrom = accFrom;
        _accTo = accTo;
        _text = text;
        _type = type;
        _date = date;
    }

    public Message(){

    }

    public Message(String accFrom,String accTo){
       _accFrom = accFrom;
        _accTo = accTo;
    }

    String _text;
    public String getMessage(){
        return _text;
    }
    public void getMessage(String newText){
        _text = newText;
    }

    Type _type;
    public Type getType(){
        return _type;
    }
    public void getType(Type newType){
        _type = newType;
    }

    Date _date;
    public Date getDate(){
        return _date;
    }
    public void getType(Date newType){
        _date = newType;
    }

    String _accTo;
    public String getAccTo(){
        return  _accTo;
    }
    public void setAccTo(String accTo){
        _accTo = accTo;
    }

    String _accFrom;
    public String getAccFrom(){
        return  _accFrom;
    }
    public void setAccFrom(String accFrom){
        _accFrom = accFrom;
    }
}
