package Core.Servlets;

import Core.Bean.Message;
import Core.Dao.AccountDao;
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
        int toID = Integer.parseInt(request.getParameter("profileTo"));

        String userName = (String) request.getSession().getAttribute("nickname");
        if (userName == null) return;

        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        MessageDao messageDao = (MessageDao) getServletContext().getAttribute(MessageDao.class.getName());

        iAccount account;
        try {
            account = accountDao.getUser(userName);
        } catch (Exception e) {
            return;
        }

        String referer = request.getHeader("Referer");

        String message = request.getParameter("message");
        if (message==null || message.equals("")) {
            return;
        }


        Message mes = new Message();
        mes.setText(message);
        mes.setAccTo(toID);
        mes.setAccFrom(account.getID());
        mes.setType(Message.Type.SENT);
        mes.setDate(new Date(System.currentTimeMillis()));

        try {
            messageDao.sendMessage(mes);
        } catch (Exception e) {
            return;
        }

        Map<Integer, Map<String, List<Message>>> unreadMessages = (Map<Integer, Map<String, List<Message>>>)
                getServletContext().getAttribute("unreadMessages");
        if (unreadMessages == null) throw new RuntimeException("Unread Messages is NULL");

        synchronized (unreadMessages) {
            Map<String, List<Message>> accountMessages = unreadMessages.get(unreadMessages.get(toID));
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
                    accountMessages.put(userName, messageList);
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
