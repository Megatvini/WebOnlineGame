package Core.Servlets;

import Core.Bean.Account;
import Core.Dao.AccountDao;
import Core.Hashing;
import Interfaces.iAccount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gukam on 5/29/2015.
 */
@WebServlet("/Registration")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        Set<String> onlineUsers = (Set<String>) getServletContext().getAttribute("onlineUsers");

        String nickName = request.getParameter("nickname");
        String password = request.getParameter("password");
        String mail = request.getParameter("mail");

        if (!isValid(nickName, password, mail, accountDao)) {
            response.sendRedirect("Accont/register.jsp");
            return;
        }

        iAccount account = new Account();
        account.setNickname(nickName);
        account.setPassword(Hashing.getHash(password));
        account.setMail(mail);
        accountDao.registerUser(account);

        request.getSession().setAttribute("nickname", account.getNickname());
        onlineUsers.add(account.getNickname());
        response.sendRedirect("index.jsp");
    }

    private boolean isValid(String nickName, String password, String mail, AccountDao accountDao) {
        try {
            accountDao.getUser(nickName);
            return false;
        } catch (Exception e) {
            return isValidMail(mail) && isValidPassword(password);
        }
    }

    private boolean isValidMail(String mail) {
        Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = p.matcher(mail);
        return m.matches();
    }

    private boolean isValidPassword(String password) {
        if (password.length()<5) return false;
        Pattern p = Pattern.compile("((\\p{Punct})*([a-zA-Z])*([0-9])*)*");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
