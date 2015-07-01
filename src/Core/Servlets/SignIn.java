package Core.Servlets;

import Core.Bean.Message;
import Core.Dao.AccountDao;
import Core.Dao.CachedMessagesDao;
import Core.Hashing;
import Interfaces.iAccount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by gukam on 5/31/2015.
 */
@WebServlet("/Login")
public class SignIn extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        if (nickname == null || password == null) {
            response.sendRedirect("Accont/Login.jsp?error=1");
            return;
        }

        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        Set<String> onlineUsers = (Set<String>) getServletContext().getAttribute("onlineUsers");

        iAccount account;
        try {
            account = accountDao.getUser(nickname);
        } catch (Exception e) {
            response.sendRedirect("Accont/Login.jsp?error=1");
            return;
        }

        if (account.getPassword().equals(Hashing.getHash(password))) {
            request.getSession().setAttribute("nickname", nickname);
            onlineUsers.add(nickname);
            takeCachedMessages(account.getID());

            Cookie cookie = new Cookie("playerID", nickname);
            response.addCookie(cookie);
            response.sendRedirect("index.jsp");
        } else {
            response.sendRedirect("Accont/Login.jsp?error=2");
        }
    }

    private void takeCachedMessages(int accId) {
        CachedMessagesDao cachedMessagesDao = (CachedMessagesDao)
                getServletContext().getAttribute(CachedMessagesDao.class.getName());
        if (cachedMessagesDao == null) throw new RuntimeException("CachedMessagesDao is NULL");

        Map<Integer, Map<String, List<Message>>> unreadMessages = (Map<Integer, Map<String, List<Message>>>)
                getServletContext().getAttribute("unreadMessages");
        if (unreadMessages == null) throw new RuntimeException("Unread Messages is NULL");

        Map<String, List<Message>> newMessageList = cachedMessagesDao.takeMessages(accId);
        if (newMessageList.size() != 0) unreadMessages.put(accId, newMessageList);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
