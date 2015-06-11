package Servlets;

import MatchMaking.StartingGroup;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Nika on 05:28, 6/11/2015.
 */
@WebServlet("/StartingGroupService")
public class StartingGroupService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    //tells user the array of userNames who are in the same creating room
    //it is called when user gets to the Create New Room Page
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, StartingGroup> groupMap = (Map<String, StartingGroup>)
                getServletContext().getAttribute(StartingGroup.class.getName());
        String userName = (String) request.getSession().getAttribute("userName");
        StartingGroup group = groupMap.get(userName);
        if (group == null) request.getRequestDispatcher("matchMaking/play.html").forward(request, response); //
        PrintWriter writer = response.getWriter();
        ArrayList<String> arr = new ArrayList<>();
        group.getGroup().forEach(arr::add);
        writer.print(arr);
    }
}
