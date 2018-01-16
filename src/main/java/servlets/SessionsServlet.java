package servlets;

import accounts.AccountService;
import accounts.DBException;
import accounts.UserProfile;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Roman on 08.01.2018.
 */
public class SessionsServlet extends HttpServlet {
    private final AccountService accountService;

    public SessionsServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    //get logged user profile
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            Gson gson = new Gson();
            String json = gson.toJson(profile);
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(json);
        }
    }

    //sign in
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");
        if (login == null || pass == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            UserProfile profile = accountService.getUserByLogin(login);
            if (profile == null || !profile.getPassword().equals(pass)) {
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            accountService.addSession(req.getSession().getId(), profile);
            Gson gson = new Gson();
            String json = gson.toJson(profile);
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    //sign out
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            accountService.deleteSession(sessionId);
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println("Goodbye!");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
