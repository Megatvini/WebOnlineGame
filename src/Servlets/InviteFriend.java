package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Nika on 05:29, 6/11/2015.
 */
@WebServlet(name = "inviteFriends", urlPatterns = {"/InviteFriend"})
public class InviteFriend extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = (String) request.getSession().getAttribute("nickname");
        String friendName = request.getParameter("friendName");

        Map<String, Map<String, Integer>> gameInvitations = (Map<String, Map<String, Integer>>)
                getServletContext().getAttribute("gameInvitations");
        if (gameInvitations == null) throw new RuntimeException("gameInvitations is NULL");

        Map<String, Integer> invitations = gameInvitations.get(friendName);
        if (invitations == null) {
            Map<String, Integer> newInvitations = new ConcurrentHashMap<>();
            //for future add only parameters client has sent
            newInvitations.put(userName, 4);
            gameInvitations.put(friendName, newInvitations);
        } else {
            invitations.put(userName, 4);
        }
        System.out.println(userName + "  " + friendName);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
