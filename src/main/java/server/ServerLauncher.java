package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import server.servlet.LoginServlet;
import server.servlet.RegistrationServlet;

public class ServerLauncher {
    public static void main(String[] args) {
        int port = 200;
        if (args.length > 0)
            try {
                port = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);



        //contextHandler.addServlet(EventServlet.class,"/*");
        contextHandler.addServlet(RegistrationServlet.class, "/reg/");
        contextHandler.addServlet(LoginServlet.class, "/login/");
        DatabaseConnector.init();
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
