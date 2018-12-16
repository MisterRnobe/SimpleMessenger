package client.network;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.concurrent.Future;

public class SocketConnection {
    private static final URI uri = URI.create("ws://localhost:200/connect/");
    private static final int MAX_MESSAGE_SIZE = 104857600;
    private static Session SESSION;
    private static WebSocketClient client;
    public static void connect()
    {
        client = new WebSocketClient();
        try
        {
            client.start();
            client.getPolicy().setMaxTextMessageSize(MAX_MESSAGE_SIZE);
            // The socket that receives events
            ClientSocket socket = new ClientSocket();
            // Attempt Connect
            Future<Session> fut = client.connect(socket,uri);
            // Wait for Connect
            SESSION = fut.get();
            // Send a message
            //session.getRemote().sendString(JSON.toJSONString(createDialog("nikita_medvedev", "Hi")));
            // Close session
            //session.close();
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }
    public static void close()
    {
        SESSION.close();
        try {
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
