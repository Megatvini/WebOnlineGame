package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Nika on 01:13, 5/29/2015.
 */
@WebServlet(name = "MatchMaker", urlPatterns = {"/MatchMaker"})
public class MatchMaker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String playerID = request.getParameter("playerName");
        System.out.println("playerName was " + playerID);
        Cookie cookie = new Cookie("playerID", playerID);
        response.addCookie(cookie);
        request.getSession().setAttribute("nickname", playerID);
        //getServletContext().getRequestDispatcher("/matchMaking/play.jsp").forward(request, response);
        response.sendRedirect("/matchMaking/play.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}