package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.AllRequestsServlet;
import servlets.MirrorServlet;

/**
 * Created by Roman on 06.01.2018.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        AllRequestsServlet allRequestsServlet = new AllRequestsServlet();
        MirrorServlet mirrorServlet = new MirrorServlet();

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(new ServletHolder(allRequestsServlet), "/*");
        contextHandler.addServlet(new ServletHolder(mirrorServlet), "/mirror");

        Server server = new Server(8080);
        server.setHandler(contextHandler);

        server.start();
        System.out.println("Server started");
        server.join();
    }
}
