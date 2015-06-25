package Core.Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

/**
 * Created by gukam on 5/31/2015.
 */
@WebServlet("/Logout")
public class SignOut extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Set<String> onlineUsers = (Set<String>) getServletContext().getAttribute("onlineUsers");
        String nickName = (String) session.getAttribute("nickname");
        session.invalidate();
        onlineUsers.remove(nickName);
        response.sendRedirect("Accont/Login.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
