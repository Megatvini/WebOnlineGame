package Core.Servlets;

import Core.Dao.AccountDao;
import Core.Dao.FriendsDao;
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
@WebServlet("/AddFriend")
public class AddToFriend extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("AddFriend: " + request.getParameterMap());

        String userName = (String) request.getSession().getAttribute("nickname");
        if (userName == null) return; //TODO gadamisamarteba login-ze

        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        FriendsDao friendsDao = (FriendsDao) getServletContext().getAttribute(FriendsDao.class.getName());

        iAccount account;
        try {
            account = accountDao.getUser(userName);
        } catch (Exception e) {
            response.sendRedirect("Accont/Login.jsp");
            return;
        }

        int IDTo = Integer.parseInt(request.getParameter("id2"));
        int IDFrom = account.getID();
        if (IDTo == IDFrom){  response.sendRedirect("Users.jsp"); return;}

        friendsDao.addFriendRequest(IDFrom, IDTo);

        response.sendRedirect("Users.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
