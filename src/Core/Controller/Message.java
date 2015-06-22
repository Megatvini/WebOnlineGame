package Core.Controller;

import java.util.Date;

/**
 * Created by Annie on 22-Jun-15.
 */
public class Message {
    public enum Type{SENT, GOTTEN}
    public Message(int accFrom,int accTo, String text, Message.Type type, Date date){
        _accFrom = accFrom;
        _accTo = accTo;
        _text = text;
        _type = type;
        _date = date;
    }

    public Message(){

    }

    public Message(int accFrom,int accTo){
       _accFrom = accFrom;
        _accTo = accTo;
    }

    String _text;
    public String getText(){
        return _text;
    }
    public void setText(String newText){
        _text = newText;
    }

    Type _type;
    public Type getType(){
        return _type;
    }
    public void setType(Type newType){
        _type = newType;
    }

    Date _date;
    public Date getDate(){
        return _date;
    }
    public void setDate(Date newDate){
        _date = newDate;
    }

    int _accTo;
    public int getAccTo(){
        return  _accTo;
    }
    public void setAccTo(int accTo){
        _accTo = accTo;
    }

    int _accFrom;
    public int getAccFrom(){ return  _accFrom; }
    public void setAccFrom(int accFrom){
        _accFrom = accFrom;
    }
}
