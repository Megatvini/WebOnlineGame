package Core.Servlets;

import Core.Bean.*;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

        Map<String, Map<String, Integer>> gameInvitations = (Map<String, Map<String, Integer>>)
                getServletContext().getAttribute("gameInvitations");
        if (gameInvitations == null) throw new RuntimeException("gameInvitations Messages is NULL");


        Set<String> friendRequestsFrom = friendsDao.getFriendRequestsTo(userAccount.getID());
        Map<String, List<Message>> unreadMessagesFrom = unreadMessages.get(userAccount.getID());
        Map<String, Integer> inviteGamesFrom = gameInvitations.get(userName);

        Notification notification = makeNotification(friendRequestsFrom, unreadMessagesFrom, inviteGamesFrom, accountDao);

        response.setContentType("application/json");
        String jsonString = new GsonBuilder().
                excludeFieldsWithoutExposeAnnotation()
                .create().toJson(notification);
        PrintWriter writer = response.getWriter();
        writer.print(jsonString);
        writer.close();
    }

    private Notification makeNotification(Set<String> friendRequestsFrom,
                                          Map<String, List<Message>> unreadMessagesFrom,
                                          Map<String, Integer> inviteGamesFrom, AccountDao accountDao) {
        List<iAccount> accountSet = getAccountSet(friendRequestsFrom, accountDao);
        List<GameInvitation> gameInvitations = getGameInvitationsMap(inviteGamesFrom, accountDao);
        List<NotificationMessage> notificationMessageMap = getNotificationMessagesMap(unreadMessagesFrom, accountDao);
        return new Notification(accountSet, gameInvitations, notificationMessageMap);
    }

    private List<NotificationMessage> getNotificationMessagesMap(Map<String, List<Message>>
                                                                                unreadMessagesFrom, AccountDao accountDao) {
        List<NotificationMessage> res = new ArrayList<>();
        if (unreadMessagesFrom == null) return res;
        unreadMessagesFrom.forEach((name, list)->{
            try {
                res.add(new NotificationMessage(accountDao.getUser(name), list));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return res;
    }

    private List<GameInvitation> getGameInvitationsMap(Map<String, Integer> inviteGamesFrom, AccountDao accountDao) {
        List <GameInvitation> res = new ArrayList<>();
        if (inviteGamesFrom == null) return res;
        inviteGamesFrom.forEach((inviteFrom, roomSize) -> {
            try {
                res.add(new GameInvitation(accountDao.getUser(inviteFrom), roomSize));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return res;
    }

    private List<iAccount> getAccountSet(Set<String> friendRequestsFrom, AccountDao accountDao) {
        List<iAccount> result = new ArrayList<>();
        if (friendRequestsFrom == null) return result;
        friendRequestsFrom.forEach(name -> {
            try {
                result.add(accountDao.getUser(name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
