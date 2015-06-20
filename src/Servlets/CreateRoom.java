package Servlets;

import MatchMaking.StartingGroup;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Nika on 03:57, 6/11/2015.
 */
@WebServlet(name = "CreateRoom", urlPatterns = {"/CreateRoom"})
public class CreateRoom extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    //it is called when user creates new room for inviting friends
    //creates new StartingGroup structure and puts it in groupMap
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession userSession = request.getSession();
        Map<String, StartingGroup> groupMap = (Map<String, StartingGroup>)
                getServletContext().getAttribute(StartingGroup.class.getName());
        if (groupMap == null) throw new RuntimeException("GroupMap is null");
        String userName = (String) userSession.getAttribute("nickname");

        if (userName == null) throw new RuntimeException("nickname is null");
        groupMap.put(userName, new StartingGroup(userName));
        response.sendRedirect("matchMaking/newroom.jsp");
    }
}
