package Core.Servlets;

import Core.Controller.Account;
import Interfaces.View.iMessageView;

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

        try {
            Account accFrom = new Account(nicknameFrom);
            accFrom.sentMessage(nicknameTo, new iMessageView.Message(message, iMessageView.Message.Type.SENT));

            Account accTo = new Account(nicknameTo);
            accTo.sentMessage(nicknameFrom, new iMessageView.Message(message, iMessageView.Message.Type.GOTTEN));

        } catch (Exception e) {
            e.printStackTrace();
        }
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
