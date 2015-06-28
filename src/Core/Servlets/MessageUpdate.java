package Core.Servlets;

import Core.Bean.Message;
import Core.Dao.AccountDao;
import Interfaces.iAccount;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by Nika on 22:30, 6/26/2015.
 */
@WebServlet(name = "MessageUpdate", urlPatterns = {"/MessageUpdate"})
public class MessageUpdate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = (String) request.getSession().getAttribute("nickname");
        String messagesFrom = request.getParameter("msgsfrom");

        if (userName == null) return;

        Map<Integer, Map<String, List<Message>>> unreadMessages = (Map<Integer, Map<String, List<Message>>>)
                getServletContext().getAttribute("unreadMessages");
        if (unreadMessages == null) throw new RuntimeException("Unread Messages is NULL");

        AccountDao accountDao = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        iAccount account;
        try {
            account = accountDao.getUser(userName);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Map<String, List<Message>> messages = unreadMessages.get(account.getID());
        if (messages == null) return;

        response.setContentType("application/json");
        String jsonString = new GsonBuilder().
                excludeFieldsWithoutExposeAnnotation()
                .create().toJson(messages);
        PrintWriter writer = response.getWriter();
        writer.print(jsonString);
        writer.close();

        if (messagesFrom != null) messages.remove(messagesFrom);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
