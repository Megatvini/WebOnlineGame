package Core.Servlets;

import Core.Controller.Account;
import Interfaces.Controller.iAccount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gukam on 6/4/2015.
 */
@WebServlet("/AcceptFriend")
public class AcceptFriendRequest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nicknameFrom = request.getParameter("nickname1");
        String nicknameTo = request.getParameter("nickname2");

        iAccount accountFrom = null;
        iAccount accountTo = null;
        try {
            accountFrom = new Account(nicknameFrom);
            accountTo = new Account(nicknameTo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            accountFrom.confirmFriend(nicknameTo);
            accountTo.addFriend(nicknameFrom);
            accountTo.confirmFriend(nicknameFrom);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("Friends.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
