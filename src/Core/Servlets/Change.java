package Core.Servlets;

import Core.Bean.Account;
import Core.Dao.AccountDao;
import Interfaces.iAccount;
import Interfaces.iProfile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gukam on 5/30/2015.
 */
@WebServlet("/ChangeAccount")
public class Change extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
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
        account.setAbout(request.getParameter("about"));
        account.setGender((request.getParameter("optionsRadios")).equals("option1") ? iAccount.Gender.MALE : iAccount.Gender.FEMALE  );
        try {
            account.setBirthDate(ConvertToDate(request.getParameter("date")));
        } catch (ParseException e) {

        }

        accountDao.changeUser(account);

        response.sendRedirect("index.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private Date ConvertToDate(String dateText) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date date = format.parse(dateText);
        return date;
    }
}
