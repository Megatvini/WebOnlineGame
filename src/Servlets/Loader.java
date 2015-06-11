package Servlets;

import MatchMaking.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Nika on 03:44, 6/11/2015.
 */
@WebServlet("/Loader")
public class Loader extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getSession().getId();
        Map<String, Collection<String>> roomMates = (Map<String, Collection<String>>) getServletContext().getAttribute("roomMates");
        boolean resp = roomMates.containsKey(userName);
        response.getWriter().print(resp);
    }
}
