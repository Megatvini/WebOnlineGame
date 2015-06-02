package Core.Servlets;

import Core.Controller.Account;

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

        HttpSession session=request.getSession();
        Account account = null;
        try {
            account = new Account((String)session.getAttribute("nickname"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        account.save();
        session.setAttribute("nickname", account.getNickname());

        account.setFirstname(request.getParameter("firstname"));
        account.setLastname(request.getParameter("lastname"));
        account.setMail(request.getParameter("mail"));
        account.setPicturePath(request.getParameter("picture"));
int x;
        account.save();

        response.sendRedirect("index.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
