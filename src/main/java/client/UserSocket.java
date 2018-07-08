package client;

import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class UserSocket extends WebSocketAdapter {
    @Override
    public void onWebSocketText(String message) {
        System.out.println("Received: "+message);
    }
}
