package window.login;

import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class ChatSocket {
    public static final String ip = "127.0.0.1";
    private Socket socket;
    private String login;
    private static ChatSocket instance;
    private ChatSocket(String login)
    {
        try {
            socket = new Socket(ip, Server.PORT);
            PrintWriter p = new PrintWriter(socket.getOutputStream());
            p.println(login);
            p.flush();
            System.out.println(System.currentTimeMillis()+" sent login!");
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        this.login = login;

    }
    public static void connect(String login)
    {
        instance = new ChatSocket(login);

    }
    public static Socket getSocket()
    {
        return instance.socket;
    }

}
