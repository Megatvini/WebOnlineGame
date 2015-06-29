/**
 * Created by Annie on 22-Jun-15.
 */

import Core.Bean.Account;
import Core.Bean.Message;
import Core.DBInfo;
import Core.Dao.AccountDao;
import Core.Dao.CachedMessagesDao;
import Core.Dao.FriendsDao;
import Core.Dao.MessageDao;
import Game.Controller.GameFactory;
import Game.Controller.GameManager;
import Game.Controller.GameServer;
import Game.Controller.UserConnector;
import MatchMaking.FixedRoomSizeMatcherFactory;
import MatchMaking.MatchingManager;
import MatchMaking.StartingGroup;
import Servlets.MatchMaker;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.accessibility.AccessibleAction;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@WebListener()
public class webListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    // Public constructor is required by servlet spec
    public webListener() {

    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(DBInfo.JDBC_DRIVER);
        ds.setUrl(DBInfo.DB_URL + "mydb?" + "user=" +DBInfo.USER+"&password=" +DBInfo.PASS);
        ds.setUsername(DBInfo.USER);
        ds.setPassword(DBInfo.PASS);

        ServletContext sc = sce.getServletContext();
        AccountDao accountDao = new AccountDao(ds);
        FriendsDao friendsDao = new FriendsDao(ds);
        MessageDao messageDao = new MessageDao(ds);
        CachedMessagesDao cachedMessagesDao = new CachedMessagesDao(ds);

        Set<String> onlineUsers = Collections.synchronizedSet(new HashSet<>());
        Map<Integer, Map<String, List<Message>>> unreadMessages = Collections.synchronizedMap(new HashMap<>());


        sc.setAttribute(AccountDao.class.getName(), accountDao);
        sc.setAttribute(FriendsDao.class.getName(), friendsDao);
        sc.setAttribute(MessageDao.class.getName(), messageDao);
        sc.setAttribute(CachedMessagesDao.class.getName(), cachedMessagesDao);
        sc.setAttribute("onlineUsers", onlineUsers);
        sc.setAttribute("unreadMessages", unreadMessages);


        Map<String, Collection<String>> roomMates = new ConcurrentHashMap<>();
        GameManager gameManager = new GameManager(roomMates, new GameFactory(), new UserConnector(),
                new ScheduledThreadPoolExecutor(GameServer.WORKING_THREAD_NUMBER));

        sce.getServletContext().setAttribute("roomMates", roomMates);
        sce.getServletContext().setAttribute(MatchMaker.class.getName(),
                new MatchingManager(roomMates, new FixedRoomSizeMatcherFactory()));
        sce.getServletContext().setAttribute(StartingGroup.class.getName(), new ConcurrentHashMap<>());
        sce.getServletContext().setAttribute(GameManager.class.getName(), gameManager);
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
        se.getSession().setMaxInactiveInterval(10 * 60); //10 minutes
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
        HttpSession session = se.getSession();
        ServletContext servletContext = session.getServletContext();
        Set<String> onlineUsers = (Set<String>) servletContext.getAttribute("onlineUsers");
        String nickName = (String) session.getAttribute("nickname");
        if (nickName != null) {
            onlineUsers.remove(nickName);
            removeCachedMessages(nickName, servletContext);
        }
    }

    private void removeCachedMessages(String nickName, ServletContext servletContext) {
        Map<Integer, Map<String, List<Message>>> unreadMessages = (Map<Integer, Map<String, List<Message>>>)
                servletContext.getAttribute("unreadMessages");
        AccountDao accountDao = (AccountDao) servletContext.getAttribute(AccountDao.class.getName());
        CachedMessagesDao cachedMessagesDao = (CachedMessagesDao) servletContext.getAttribute(CachedMessagesDao.class.getName());

        if (unreadMessages == null) throw new RuntimeException("Unread Messages is NULL");
        if (accountDao == null) throw new RuntimeException("AccountDao is NULL");
        try {
            int accID = accountDao.getUser(nickName).getID();
            cachedMessagesDao.addMessages(accID, unreadMessages.get(accID));
            unreadMessages.remove(accID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attibute
         is replaced in a session.
      */
    }
}
