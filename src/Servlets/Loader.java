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
@WebServlet(name = "Loader", urlPatterns = {"/Loader"})
public class Loader extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    //users call loader to know if their match has been found
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = (String) request.getSession().getAttribute("nickname");
        if (userName == null) throw new RuntimeException("nickname was null");

        Map<String, Collection<String>> roomMates = (Map<String, Collection<String>>) getServletContext().getAttribute("roomMates");
        if (roomMates == null) throw new RuntimeException("roomMates is null");
        boolean resp = roomMates.containsKey(userName);
        response.getWriter().print(resp);
    }
}
