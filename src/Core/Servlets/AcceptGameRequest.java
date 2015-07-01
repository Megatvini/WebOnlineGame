package Core.Servlets;

import MatchMaking.StartingGroup;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Nika on 09:31, 7/1/2015.
 */
@WebServlet(name = "AcceptGameRequest", urlPatterns = "/AcceptGameRequest")
public class AcceptGameRequest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = (String) request.getSession().getAttribute("nickname");
        String senderAccount = request.getParameter("senderAccount");
        if (userName == null) {
            response.sendRedirect("Accont/Login.jsp");
            return;
        }

        Map<String, StartingGroup> groupMap = (Map<String, StartingGroup>)
                getServletContext().getAttribute(StartingGroup.class.getName());
        if (groupMap == null) throw new RuntimeException("groupMap is null");

        Map<String, Map<String, Integer>> gameInvitations = (Map<String, Map<String, Integer>>)
                getServletContext().getAttribute("gameInvitations");

        if (gameInvitations == null) throw new RuntimeException("gameInvitations Messages is NULL");
        if (gameInvitations.get(userName) == null || !gameInvitations.get(userName).containsKey(senderAccount)) {
            response.sendRedirect("index.jsp");
            return;
        }


        //TODO Checks
        if (groupMap.containsKey(senderAccount)) {
            if (groupMap.get(senderAccount).getGroup().size() < 4) {
                groupMap.get(senderAccount).addUser(userName);
                groupMap.put(userName, groupMap.get(senderAccount));
                gameInvitations.get(userName).remove(senderAccount);
                response.sendRedirect("JoinRoom.jsp");
            } else {
                response.sendRedirect("index.jsp");
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }
}
