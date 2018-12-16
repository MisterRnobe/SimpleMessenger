package server;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.servlet.EventServlet;

@Slf4j
public class ServerLauncher {
    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("Загрузил бины");
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
        log.info("WOW!");

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);



        contextHandler.addServlet(EventServlet.class,"/connect/");

        try {
            server.start();
            System.out.println("Server is up!");
            server.join();
            System.out.println("Server is down!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static ClassPathXmlApplicationContext context;

    public static ClassPathXmlApplicationContext getContext() {
        return context;
    }
}
