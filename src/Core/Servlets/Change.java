package Core.Servlets;

import Core.Controller.Account;
import Core.Model.UserControl;
import Interfaces.Controller.iAccount;

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
        UserControl userControl = (UserControl)getServletContext().getAttribute("userControl");
        HttpSession session=request.getSession();
        iAccount account = null;
        try {

            account = userControl.getUser((session.getAttribute("nickname")).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.setAttribute("nickname", account.getNickname());

        account.setFirstname(request.getParameter("firstname"));
        account.setLastname(request.getParameter("lastname"));
        account.setMail(request.getParameter("mail"));
        account.setPicturePath(request.getParameter("picture"));

        userControl.changeUser(account.getNickname(),account);

        response.sendRedirect("index.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
