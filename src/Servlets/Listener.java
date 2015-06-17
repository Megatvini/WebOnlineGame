package Servlets;
/**
 * Created by Nika on 17:26, 5/26/2015.
 */
import Game.Controller.GameFactory;
import Game.Controller.GameManager;
import Game.Controller.GameServer;
import Game.Controller.UserConnector;
import MatchMaking.FixedRoomSizeMatcherFactory;
import MatchMaking.MatchingManager;
import MatchMaking.StartingGroup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@WebListener()
public class Listener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    // Public constructor is required by servlet spec
    public Listener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
        Map<String, Collection<String>> roomMates = Collections.synchronizedMap(new HashMap<>());
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
