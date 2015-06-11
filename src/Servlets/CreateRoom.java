package Servlets;

import MatchMaking.StartingGroup;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Nika on 03:57, 6/11/2015.
 */
@WebServlet("/CreateRoom")
public class CreateRoom extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, StartingGroup> groupMap = (Map<String, StartingGroup>)
                getServletContext().getAttribute(StartingGroup.class.getName());
        String userName = request.getSession().getId();
        groupMap.put(userName, new StartingGroup(userName));
        request.getRequestDispatcher("matchMaking/newroom.jsp").forward(request, response);
    }
}
