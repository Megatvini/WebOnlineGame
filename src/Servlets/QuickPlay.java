package Servlets;

import MatchMaking.*;
import MatchMaking.MatchMaker;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nika on 03:15, 6/11/2015.
 */
@WebServlet("/QuickPlay")
public class QuickPlay extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/matchMaking/loading.html");
        dispatcher.forward(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getSession().getId();
        List<Integer> list = new ArrayList<>();
        for (int i=2; i<=4; i++) {
            String s = request.getParameter("roomsize"+i);
            if (s!= null && s.equals("on"))
                list.add(i);
        }
        MatchMaker matchMaker = (MatchMaker) getServletContext().getAttribute(Servlets.MatchMaker.class.getName());
        matchMaker.addParticipant(userName, list);
    }
}