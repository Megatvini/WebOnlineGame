package Servlets;

import MatchMaking.StartingGroup;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Nika on 06:13, 6/11/2015.
 */
@WebServlet(name = "StartGame", urlPatterns = {"/StartGame"})
public class StartGame extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    //process request from new room creator to start the game
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MatchMaking.MatchMaker matchMaker = (MatchMaking.MatchMaker) getServletContext().getAttribute(MatchMaker.class.getName());
        Map<String, StartingGroup> groupMap = (Map<String, StartingGroup>)
                getServletContext().getAttribute(StartingGroup.class.getName());
        if (groupMap == null) throw new RuntimeException("groupMap is null");
        String userName = (String) request.getSession().getAttribute("nickname");


        Set<Integer> roomSizes = new HashSet<>();
        Set<String> arbitraryRoomMates = new HashSet<>();
        readParameters(request, arbitraryRoomMates, roomSizes);

        if (roomSizes.size() == 0) {
            response.sendRedirect("/matchMaking/play.jsp");
            return;
        }

        if (!validate(roomSizes, arbitraryRoomMates)) {
            response.sendRedirect("play.jsp");
        } else {
            matchMaker.addParticipants(arbitraryRoomMates, roomSizes);
            groupMap.get(userName).setGameStarted(true);
            response.sendRedirect("matchMaking/loading.jsp");
        }
    }

    //reads path parameters and fills arbitraryRoomMates and roomSizes with
    //corresponding information
    private void readParameters(HttpServletRequest request, Set<String> arbitraryRoomMates, Set<Integer> roomSizes) {
        for (int i=2; i<=4; i++) {
            String s = request.getParameter("roomsize" + i);
            if (s!=null && s.equals("on")) roomSizes.add(i);
        }

        for (int i=1; i<=4; i++) {
            String s = request.getParameter("p"+i);
            if (s!=null && !s.equals("Empty"))
                arbitraryRoomMates.add(s);
        }
    }

    //validates the input from path parameters
    private boolean validate(Set<Integer> roomSizes, Set<String> arbitraryRoomMates) {
        switch (arbitraryRoomMates.size()) {
            case 0: return false;
            case 1:case 2 : return true;
            case 3:
                if (roomSizes.contains(2)) return false;
            case 4:
                if (roomSizes.contains(2)) return false;
                if (roomSizes.contains(3)) return false;
        }
        return false;
    }

}
