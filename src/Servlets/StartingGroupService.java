package Servlets;

import MatchMaking.StartingGroup;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Nika on 05:28, 6/11/2015.
 */
@WebServlet(name = "StartingGroupService", urlPatterns = {"/StartingGroupService"})
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
        //System.out.println("StartingGroupService:" + request.getSession().getId());

        Map<String, StartingGroup> groupMap = (Map<String, StartingGroup>)
                getServletContext().getAttribute(StartingGroup.class.getName());
        if (groupMap == null) throw new RuntimeException("groupMap is null");

        String userName = (String) request.getSession().getAttribute("nickname");
        if (userName == null) throw new RuntimeException("nickname was null");

        StartingGroup group = groupMap.get(userName);
        if (group == null) response.sendRedirect("matchMaking/play.jsp");
        PrintWriter writer = response.getWriter();
        ArrayList<String> arr = new ArrayList<>();
        if (!group.isGameStarted()) {
            group.getGroup().forEach(arr::add);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String jsonString = new GsonBuilder().
                excludeFieldsWithoutExposeAnnotation()
                .create().toJson(arr);
        writer.print(jsonString);
        writer.close();
    }
}
