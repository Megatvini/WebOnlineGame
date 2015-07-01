package Core.Servlets;

import Core.Bean.Message;
import Core.Dao.AccountDao;
import Core.Dao.CachedMessagesDao;
import Core.Dao.MessageDao;
import Interfaces.iAccount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by gukam on 6/3/2015.
 */
@WebServlet("/SendMessage")
public class SentMessageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int toID = Integer.parseInt(request.getParameter("profileTo"));

        String userName = (String) request.getSession().getAttribute("nickname");
        if (userName == null) return;

        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        MessageDao messageDao = (MessageDao) getServletContext().getAttribute(MessageDao.class.getName());
        Set<String> onlineUsers = (Set<String>) getServletContext().getAttribute("onlineUsers");

        iAccount accountFrom;
        iAccount accountTo;
        try {
            accountFrom = accountDao.getUser(userName);
            accountTo = accountDao.getUser(toID);
        } catch (Exception e) {
            return;
        }

        String message = request.getParameter("message");
        if (message==null || message.equals("")) {
            return;
        }


        Message mes = new Message();
        mes.setText(message);
        mes.setAccTo(accountTo.getID());
        mes.setAccFrom(accountFrom.getID());
        mes.setType(Message.Type.SENT);
        mes.setDate(new Date(System.currentTimeMillis()));

        try {
            messageDao.sendMessage(mes);
        } catch (Exception e) {
            return;
        }

        if (onlineUsers.contains(accountTo.getNickname())) {
            saveMessage(mes, userName, accountTo.getID());
        } else {
            saveToDatabase(accountFrom, accountTo, mes);
        }
    }

    private void saveToDatabase(iAccount accountFrom, iAccount accountTo, Message mes) {
        CachedMessagesDao cachedMessagesDao = (CachedMessagesDao)
                getServletContext().getAttribute(CachedMessagesDao.class.getName());
        if (cachedMessagesDao == null) throw new RuntimeException("CachedMessagesDao is NULL");
        cachedMessagesDao.addSingleMessage(accountTo.getID(), accountFrom.getNickname(), mes.getText(), mes.getDate());
    }

    private void saveMessage(Message mes, String userName, Integer toID) {
        Map<Integer, Map<String, List<Message>>> unreadMessages = (Map<Integer, Map<String, List<Message>>>)
                getServletContext().getAttribute("unreadMessages");
        if (unreadMessages == null) throw new RuntimeException("Unread Messages is NULL");

        synchronized (unreadMessages) {
            Map<String, List<Message>> accountMessages = unreadMessages.get(toID);
            if (accountMessages == null) {
                accountMessages = Collections.synchronizedMap(new HashMap<>());
                List<Message> messageList = Collections.synchronizedList(new ArrayList<>());
                messageList.add(mes);
                accountMessages.put(userName, messageList);
                unreadMessages.put(toID, accountMessages);
            } else {
                List<Message> messageList = accountMessages.get(userName);
                if (messageList == null) {
                    messageList = Collections.synchronizedList(new ArrayList<>());
                    messageList.add(mes);
                    accountMessages.put(userName, messageList);
                } else {
                    messageList.add(mes);
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
