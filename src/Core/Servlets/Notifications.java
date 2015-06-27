package Core.Servlets;

import Core.Bean.Account;
import Core.Bean.Message;
import Core.Bean.Notification;
import Core.Dao.AccountDao;
import Core.Dao.FriendsDao;
import Interfaces.iAccount;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Nika on 19:38, 6/27/2015.
 */
@WebServlet(name = "Notifications", urlPatterns = {"/NotificationsUpdate"})
public class Notifications extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = (String) request.getSession().getAttribute("nickname");
        FriendsDao friendsDao = (FriendsDao) getServletContext().getAttribute(FriendsDao.class.getName());
        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());

        iAccount userAccount;
        try {
            userAccount = accountDao.getUser(userName);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Map<Integer, Map<String, List<Message>>> unreadMessages = (Map<Integer, Map<String, List<Message>>>)
                getServletContext().getAttribute("unreadMessages");
        if (unreadMessages == null) throw new RuntimeException("Unread Messages is NULL");

        Set<String> friendRequestsFrom = friendsDao.getFriendRequestsTo(userAccount.getID());
        Map<String, List<Message>> unreadMessagesFrom = unreadMessages.get(userAccount.getID());
        Map<String, Integer> inviteGamesFrom = new HashMap<>(); //TODO get it from servletContext

        Notification notification = new Notification(friendRequestsFrom, inviteGamesFrom, unreadMessagesFrom);

        response.setContentType("application/json");
        String jsonString = new GsonBuilder().
                excludeFieldsWithoutExposeAnnotation()
                .create().toJson(notification);
        PrintWriter writer = response.getWriter();
        writer.print(jsonString);
        writer.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
