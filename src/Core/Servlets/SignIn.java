package Core.Servlets;


import Core.Controller.Hashing;
import Core.Model.Dao.AccountDao;
import Interfaces.iAccount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by gukam on 5/31/2015.
 */
@WebServlet("/Login")
public class SignIn extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");

        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        Set<String> onlineUsers = (Set<String>) getServletContext().getAttribute("onlineUsers");


        iAccount account;
        try {
            account = accountDao.getUser(nickname);
        } catch (Exception e) {
            //TODO redirect to wrong parameters
            //response.sendRedirect("Accont/Login.jsp?error=2");
            return;
        }

        if (Hashing.getHash(account.getPassword()).equals(Hashing.getHash(password))) {
            request.getSession().setAttribute("nickName", nickname);
            onlineUsers.add(nickname);
            response.sendRedirect("index.jsp");
        } else {
            //TODO Send redirect with wrong username or password
            //response.sendRedirect("Accont/Login.jsp?error=2");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
