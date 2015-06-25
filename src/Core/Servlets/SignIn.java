package Core.Servlets;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gukam on 5/31/2015.
 */
@WebServlet("/Login")
public class SignIn extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");

//        //UserControl userControl = (UserControl)getServletContext().getAttribute("userControl");
//        iAccount accaunt;
//
//        boolean logined = false;
//        try {
//            accaunt = userControl.getUser(nickname);
//            logined = accaunt.getPassword().equals(password);
//        } catch (Exception e) {
//            response.sendRedirect("Accont/Login.jsp?error=1");
//            return;
//        }
//
//        if (logined) {
//            HttpSession session = request.getSession();
//            session.setAttribute("nickname", nickname);
//            response.sendRedirect("index.jsp");
//            userControl.addOnlineUser(accaunt.getID());
//        }
//        else {
//            response.sendRedirect("Accont/Login.jsp?error=2");
//        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
