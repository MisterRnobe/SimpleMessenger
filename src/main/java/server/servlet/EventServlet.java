package server.servlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class EventServlet extends WebSocketServlet{
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.register(EventSocket.class);
    }
}
