package server.servlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class EventServlet extends WebSocketServlet{
    public static final int MAX_MESSAGE_SIZE = 104857600;
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.getPolicy().setMaxTextMessageSize(MAX_MESSAGE_SIZE);
        webSocketServletFactory.register(EventSocket.class);
    }
}
