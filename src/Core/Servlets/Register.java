package Core.Servlets;

import Core.Model.Bean.Account;
import Core.Controller.Hashing;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gukam on 5/29/2015.
 */
@WebServlet("/Registration")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Account account = new Account();
//        account.setNickname(request.getParameter("nickname"));
//        account.setMail(request.getParameter("mail"));
//        account.setPassword(Hashing.getHash(request.getParameter("password")));
//        UserControl userControl = (UserControl)getServletContext().getAttribute("userControl");
//        userControl.registerUser(account);
//
//        HttpSession session = request.getSession();
//        session.setAttribute("nickname", account.getNickname());

        response.sendRedirect("index.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
