package main;

import accounts.AccountService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.*;

/**
 * Created by Roman on 06.01.2018.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        AccountService accountService = new AccountService();
//        accountService.addNewUser(new UserProfile("admin"));
//        accountService.addNewUser(new UserProfile("test"));

        AllRequestsServlet allRequestsServlet = new AllRequestsServlet();
        MirrorServlet mirrorServlet = new MirrorServlet();

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(new ServletHolder(allRequestsServlet), "/*");
        contextHandler.addServlet(new ServletHolder(mirrorServlet), "/mirror");
        contextHandler.addServlet(new ServletHolder(new UsersServlet(accountService)), "/api/v1/users");
        contextHandler.addServlet(new ServletHolder(new SessionsServlet(accountService)), "/api/v1/sessions");
        contextHandler.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        contextHandler.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {resourceHandler, contextHandler});

        Server server = new Server(8080);
        server.setHandler(handlers);

        server.start();
        accountService.createUserTable();
        System.out.println("Server started");
        server.join();
    }
}
