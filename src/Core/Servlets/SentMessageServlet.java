package Core.Servlets;

import Core.Model.Bean.Message;
import Core.Model.Dao.AccountDao;
import Core.Model.Dao.MessageDao;
import Interfaces.iAccount;

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
        int toID = Integer.parseInt(request.getParameter("profileTo"));

        String userName = (String) request.getSession().getAttribute("nickname");
        if (userName == null) return;

        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        MessageDao messageDao = (MessageDao) getServletContext().getAttribute(MessageDao.class.getName());


        iAccount account;
        try {
            account = accountDao.getUser(userName);
        } catch (Exception e) {
            //TODO redirect to error page
            return;
        }


        String message = request.getParameter("message");

        System.out.println("SEND MESSAGE: " + request.getParameterMap());

        Message mes = new Message();
        mes.setText(message);
        mes.setAccTo(toID);
        mes.setAccFrom(account.getID());
        mes.setType(Message.Type.SENT);
        //TODO date

        try {
            messageDao.sendMessage(mes);
        } catch (Exception e) {
            return;
        }

        String referer = request.getHeader("Referer");
        response.sendRedirect(referer);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
