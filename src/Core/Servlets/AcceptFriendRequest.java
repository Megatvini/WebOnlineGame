package Core.Servlets;

import Core.Controller.Account;
import Core.Model.UserControl;
import Interfaces.Controller.iAccount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by gukam on 6/4/2015.
 */
@WebServlet("/AcceptFriend")
public class AcceptFriendRequest extends HttpServlet {
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

        try {
            userControl.confirmFriendRequest(IDFrom, IDTo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("Friends.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
