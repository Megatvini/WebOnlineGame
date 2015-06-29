package Core.Bean;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Created by Annie on 22-Jun-15.
 */
public class Message {
    private int accFrom;
    private int accTo;
    private String header;
    @Expose
    private String text;
    @Expose
    private Date date;
    public enum Type{SENT, GOTTEN}

    public Message(int accFrom, int accTo, String text, String header, Date date){
        this.accFrom = accFrom;
        this.accTo = accTo;
        this.text = text;
        this.header = header;
        this.date = date;
    }

    public Message(){

    }

    public Message(int accFrom,int accTo){
       this.accFrom = accFrom;
        this.accTo = accTo;
    }

    public String getText(){
        return text;
    }
    public void setText(String newText){
        text = newText;
    }

    public Date getDate(){
        return date;
    }
    public void setDate(Date newDate){
        date = newDate;
    }

    public int getAccTo(){
        return accTo;
    }
    public void setAccTo(int accTo){
        this.accTo = accTo;
    }

    public int getAccFrom(){ return accFrom; }
    public void setAccFrom(int accFrom){
        this.accFrom = accFrom;
    }

    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }

    Type _type;
    public Type getType(){
        return _type;
    }
    public void setType(Type newType){
        _type = newType;
    }
}
