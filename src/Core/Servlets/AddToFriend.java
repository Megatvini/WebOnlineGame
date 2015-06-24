package Core.Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

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
        UserControl userControl;
        userControl = (UserControl)getServletContext().getAttribute("userControl");

        int IDFrom = 0;
        try {
            IDFrom = userControl.getID(request.getParameter("id1"));
        } catch (SQLException e) {
           //TODO
        }
        int IDTo = Integer.parseInt(request.getParameter("id2"));

        userControl.addFriend(IDFrom, IDTo);
        response.sendRedirect("Users.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
