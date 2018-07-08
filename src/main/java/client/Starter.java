package client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Methods;
import common.Request;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.TreeMap;
import java.util.concurrent.Future;

public class Starter {
    public static final String login = "nikita_medvedev";
    public static final String password = "123456";
    private static Session session;
    public static void main(String[] args) throws Exception {
        String token = auth();
        connect(login, token);
        session.getRemote().sendString(JSONObject.toJSONString(
                sendMessage("8","Some text lol")
        ));

//        Request r = createDialog("nikita_medvedev", "Doroy");
//        session.getRemote().sendString(JSON.toJSONString(r));
//        for (int i = 0; i < 3; i++) {
//            Request r = createDialog("nikita"+Integer.toString(i+1), "Hello("+Integer.toString(i)+")");
//            session.getRemote().sendString(JSON.toJSONString(r));
//        }


    }
    public static String auth() throws Exception {
        String url = "http://localhost:200/login/";//"http://nikitamedvedev.ddns.net";
        HttpClient httpClient = new HttpClient();
        httpClient.start();
        ContentResponse response = httpClient.POST(url)
                .param("login", login)
                .param("password", password)
                .send();
        //System.out.println(response.getContentAsString());
        JSONObject o = JSON.parseObject(response.getContentAsString());
        String token = o.getJSONObject("body").getString("token");//.get("token");
        httpClient.stop();
        return token;
    }
    public static void reg(String login, String mail) throws Exception {
        String url = "http://localhost:200/reg/";//"http://nikitamedvedev.ddns.net";
        HttpClient httpClient = new HttpClient();
        httpClient.start();
        ContentResponse response = httpClient.POST(url)
                .param("login",login)
                .param("password","123456")
                .param("name","Nikita Medvedev")
                .param("email",mail)
                .param("info","Cool!")
                .send();
        System.out.println(response.getContentAsString());
        httpClient.stop();
    }
    public static void connect(String login, String token)
    {
        URI uri = URI.create("ws://localhost:200/connect/");
        WebSocketClient client = new WebSocketClient();
        try
        {
            try
            {
                client.start();
                // The socket that receives events
                UserSocket socket = new UserSocket();
                // Attempt Connect
                Future<Session> fut = client.connect(socket,uri);
                // Wait for Connect
                session = fut.get();
                // Send a message
                session.getRemote().sendString(JSON.toJSONString(new Request().setMethod(Methods.AUTH)
                        .setBody(new TreeMap<String, String>(){{this.put("token", token);this.put("login", login);}})));
                //session.getRemote().sendString(JSON.toJSONString(createDialog("nikita_medvedev", "Hi")));
                // Close session
                //session.close();
            }
            finally
            {
                //client.stop();
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }
    public static Request createDialog(String partner, String initMsg)
    {
        Request r = new Request();
        r.setMethod(Methods.DIALOG_CREATION).setBody(new TreeMap<String, String>(){{
            this.put("creator", login);
            this.put("partner", partner);
            this.put("initialMessage", initMsg);
        }});
        return r;
    }
    public static Request sendMessage(String dialogId, String text)
    {
        Request r = new Request();
        r.setMethod(Methods.SEND_MESSAGE).setBody(
                new TreeMap<String, String>(){{
                    this.put("login", login);
                    this.put("dialog_id", dialogId);
                    this.put("text", text);
                }}
        );
        return r;
    }
}
