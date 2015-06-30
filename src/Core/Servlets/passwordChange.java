package Core.Servlets;

import Core.Dao.AccountDao;
import Core.Hashing;
import Interfaces.iAccount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by rezo on 6/30/15.
 */
@WebServlet(name = "passwordChange", urlPatterns = "/passwordChange")
public class passwordChange extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        String nickname = (String) request.getSession().getAttribute("nickname");
        if (nickname==null){
            response.sendRedirect("/Accont/Login.jsp");
            return;
        }
        iAccount account = null;
        try {
            account = accountDao.getUser(nickname);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        if (account==null) {
            return;
        }

        String oldPass = request.getParameter("oldPass");
        String newPass = request.getParameter("newPass");

        response.setContentType("text/plain");
        String responseText = "";

        if (account.getPassword().equals(Hashing.getHash(oldPass))){
            if(Register.isValidPassword(newPass)){
                account.setPassword(Hashing.getHash(newPass));
                accountDao.changeUser(account);
                responseText="1";
            }else{
                responseText="2";

            }
        }else {
            responseText = "3";

        }

      response.getOutputStream().print(responseText);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getOutputStream().println("invalid invocation");
    }
}
