package Core.Servlets;

import Core.Bean.Account;
import Core.Dao.AccountDao;
import Interfaces.iAccount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by gukam on 5/30/2015.
 */
@WebServlet("/ChangeAccount")
public class Change extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("nickname");
        if (userName == null) return;

        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());

        iAccount account;
        try {
            account = accountDao.getUser(userName);
        } catch (Exception e) {
            response.sendRedirect("Accont/Login.jsp");
            return;
        }

        account.setFirstName(request.getParameter("firstname"));
        account.setLastName(request.getParameter("lastname"));
        account.setPicturePath(request.getParameter("picture"));
        account.setAbout(request.getParameter("about"));
        //TODO BirthDate and Gender

        accountDao.changeUser(account);

        response.sendRedirect("index.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
