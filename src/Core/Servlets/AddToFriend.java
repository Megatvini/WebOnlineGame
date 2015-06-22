package Core.Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import Core.Controller.Account;
import Core.Model.DBWorker;
import Core.Model.UserControl;
import Interfaces.Controller.iAccount;

/**
 * Created by gukam on 6/3/2015.
 */
@WebServlet("/AddFriend")
public class AddToFriend extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nicknameFrom = request.getParameter("nickname1");
        String nicknameTo = request.getParameter("nickname2");

        iAccount accountFrom = null;
        iAccount accountTo = null;
        try {
            UserControl userControl = (UserControl)getServletContext().getAttribute("userControl");
            accountFrom = userControl.getUser(nicknameFrom);
            accountTo = userControl.getUser(nicknameTo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            accountTo.addFriend(nicknameFrom);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("Users.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
