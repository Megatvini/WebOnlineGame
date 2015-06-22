package Core.Servlets;

import Core.Model.UserControl;
import Interfaces.Controller.iAccount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gukam on 6/3/2015.
 */
@WebServlet("/SendMessage")
public class SentMessageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nicknameFrom = request.getParameter("profileFrom");
        String nicknameTo = request.getParameter("profileTo");
        String message = request.getParameter("message");
        UserControl userControl = (UserControl)getServletContext().getAttribute("userControl");

        try {
            iAccount accFrom = userControl.getUser(nicknameFrom);
            //TODO:

            iAccount accTo = userControl.getUser(nicknameTo);
            //TODO:

        } catch (Exception e) {
            e.printStackTrace();
        }
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
