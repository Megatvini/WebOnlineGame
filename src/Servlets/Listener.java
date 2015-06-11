package Servlets;
/**
 * Created by Nika on 17:26, 5/26/2015.
 */
import MatchMaking.MatchMakerMock;
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
        Map<String, Collection<String>> roomMates = new ConcurrentHashMap<>();
        List<String> room1 = new ArrayList<>();
        room1.add("room1player1");
        room1.add("room1player2");
       // room1.add("room1player3");

        List<String> room2 = new ArrayList<>();
        room2.add("room2player1");
        room2.add("room2player2");
        room2.add("room2player3");
        room2.add("room2player4");

        room1.forEach(x->roomMates.put(x, room1));
        room2.forEach(x->roomMates.put(x, room2));


        sce.getServletContext().setAttribute("roomMates", roomMates);
        sce.getServletContext().setAttribute(MatchMaker.class.getName(), new MatchMakerMock(roomMates));
        sce.getServletContext().setAttribute(StartingGroup.class.getName(), new ConcurrentHashMap<>());
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
