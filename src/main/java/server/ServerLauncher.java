package server;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import server.servlet.EventServlet;

@Slf4j
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

        contextHandler.addServlet(EventServlet.class,"/connect/");
        //DatabaseConnectorOld.init();
        try {
            server.start();
            log.info("Server is up!");
            server.join();
            log.info("Server is down!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
