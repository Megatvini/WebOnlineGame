package Core.Servlets;

import Core.AuthenticationManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by gukam on 5/31/2015.
 */
@WebServlet("/Login")
public class SignIn extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");

        boolean logined = AuthenticationManager.login(nickname, password);

        if (logined) {
            HttpSession session = request.getSession();
            session.setAttribute("nickname", nickname);
            response.sendRedirect("index.jsp");
        }
        else {
            response.sendRedirect("Accont/Login.jsp");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
