package Core.Servlets;

import Core.Model.Bean.Account;
import Core.Controller.Hashing;
import Core.Model.Dao.AccountDao;
import Core.Model.Dao.FriendsDao;
import Interfaces.iAccount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by gukam on 5/29/2015.
 */
@WebServlet("/Registration")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        Set<String> onlineUsers = (Set<String>) getServletContext().getAttribute("onlineUsers");

        iAccount account = new Account();
        //TODO get parameters from request and init Account
        //TODO check if account with username is already registered
        accountDao.registerUser(account);

        request.getSession().setAttribute("nickname", account.getNickname());
        onlineUsers.add(account.getNickname());
        response.sendRedirect("index.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
