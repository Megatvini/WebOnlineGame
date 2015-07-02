package Core.Bean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Nika on 03:25, 6/26/2015.
 */
public class MessageTest {
    @Test
    public void testText() throws Exception {
        Message message = new Message();
        message.setText("text");
        assertTrue(message.getText().equals("text"));
        message.setText("changedText");
        assertTrue(message.getText().equals("changedText"));
    }

    @Test
    public void testDate() throws Exception {
        Message message = new Message();
        Date date = new Date(System.currentTimeMillis());
        message.setDate(date);
        assertEquals(date, message.getDate());
    }

    @Test
    public void testAccTo() throws Exception {
        Message message = new Message();
        message.setAccTo(1);
        assertEquals(1, message.getAccTo());
        message.setAccTo(2);
        assertEquals(2, message.getAccTo());
    }

    @Test
    public void testAccFrom() throws Exception {
        Message message = new Message();
        message.setAccFrom(3);
        assertEquals(3, message.getAccFrom());
        message.setAccFrom(17);
        assertEquals(17, message.getAccFrom());
    }


    @Test
    public void testSetHeader() throws Exception {
        Message message = new Message();
        message.setHeader("head");
        assertTrue(message.getHeader().equals("head"));
        message.setHeader("newHead");
        assertTrue(message.getHeader().equals("newHead"));
    }

    @Test
    public void testSetType() throws Exception {
        Message message = new Message();
        message.setType(Message.Type.GOTTEN);
        assertEquals(Message.Type.GOTTEN, message.getType());
        message.setType(Message.Type.SENT);
        assertEquals(Message.Type.SENT, message.getType());
    }

    @Test
    public void combinedTest() {
        Message message = new Message(1, 2);
        assertEquals(1, message.getAccFrom());
        assertEquals(2, message.getAccTo());
        message.setAccFrom(4);
        assertEquals(4, message.getAccFrom());
    }

    @Test
    public void combinedTest1() {
        Date date = new Date();
        Message message = new Message(3, 100, "msg", "header", date);
        assertEquals(3, message.getAccFrom());
        assertEquals(100, message.getAccTo());
        assertTrue(message.getText().equals("msg"));
        assertTrue(message.getHeader().equals("header"));
        assertEquals(date, message.getDate());
    }
}