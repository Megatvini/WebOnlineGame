package Core.Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import Core.Controller.Account;
import Interfaces.Controller.iAccount;

/**
 * Created by gukam on 6/3/2015.
 */
@WebServlet("/AddFriend")
public class AddToFriend extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nicknameFrom = request.getParameter("nickname1");
        String nicknameTo = request.getParameter("nickname2");

        iAccount account = null;
        try {
            account = new Account(nicknameFrom);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            account.addFriend(nicknameTo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("Users.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
