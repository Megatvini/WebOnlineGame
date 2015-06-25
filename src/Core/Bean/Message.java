package Core.Bean;

import java.util.Date;

/**
 * Created by Annie on 22-Jun-15.
 */
public class Message {
    private int _accFrom;
    private int _accTo;
    private String _header;
    private String _text;
    private Date _date;
    public enum Type{SENT, GOTTEN}

    public Message(int accFrom, int accTo, String text, String header, Date date){
        _accFrom = accFrom;
        _accTo = accTo;
        _text = text;
        _header = header;
        _date = date;
    }

    public Message(){

    }

    public Message(int accFrom,int accTo){
       _accFrom = accFrom;
        _accTo = accTo;
    }

    public String getText(){
        return _text;
    }
    public void setText(String newText){
        _text = newText;
    }

    public Date getDate(){
        return _date;
    }
    public void setDate(Date newDate){
        _date = newDate;
    }

    public int getAccTo(){
        return  _accTo;
    }
    public void setAccTo(int accTo){
        _accTo = accTo;
    }

    public int getAccFrom(){ return  _accFrom; }
    public void setAccFrom(int accFrom){
        _accFrom = accFrom;
    }

    public String getHeader() {
        return _header;
    }
    public void setHeader(String header) {
        _header = header;
    }

    Type _type;
    public Type getType(){
        return _type;
    }
    public void setType(Type newType){
        _type = newType;
    }
}
