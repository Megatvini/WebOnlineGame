/**
 * Created by Annie on 22-Jun-15.
 */

import Core.DBInfo;
import Core.Dao.AccountDao;
import Core.Dao.FriendsDao;
import Core.Dao.MessageDao;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
        Set<String> onlineUsers = Collections.synchronizedSet(new HashSet<>());

        sc.setAttribute(AccountDao.class.getName(), accountDao);
        sc.setAttribute(FriendsDao.class.getName(), friendsDao);
        sc.setAttribute(MessageDao.class.getName(), messageDao);
        sc.setAttribute("onlineUsers", onlineUsers);
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
    }

    public void sessionDestroyed(HttpSessionEvent se) {
      /* Session is destroyed. */
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
