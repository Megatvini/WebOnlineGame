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
@WebServlet("/MatchMaker")
public class MatchMaker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String playerID = request.getParameter("playerName");
        System.out.println("playerName was " + playerID);
        Cookie cookie = new Cookie("playerID", playerID);
        response.addCookie(cookie);
        request.getSession().setAttribute("userName", playerID);
        request.getRequestDispatcher("matchMaking/play.html").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}