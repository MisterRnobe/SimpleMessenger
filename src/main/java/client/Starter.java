package client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.DialogList;
import common.objects.Message;
import common.objects.requests.CreateDialogRequest;
import common.objects.requests.DialogListRequest;
import common.objects.requests.RegistrationData;
import common.objects.requests.UserPasswordData;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import server.core.GetDialogsHandler;

import java.net.URI;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Future;

public class Starter {
    public static final String login = "nikita_medvedev";
    public static final String password = "123456";
    private static Session session;
    public static void main(String[] args) {
        String json = "{\"body\":{\"dialogs\":[{\"creator\":\"nikita1\",\"dialogId\":8,\"lastMessage\":{\"dialogId\":8,\"messageId\":9,\"sender\":\"nikita_medvedev\",\"text\":\"Some text lol\",\"time\":1531062543919}},{\"creator\":\"nikita_medvedev\",\"dialogId\":9,\"lastMessage\":{\"dialogId\":9,\"messageId\":11,\"sender\":\"nikita_medvedev\",\"text\":\"Tectum\",\"time\":1531912106765}}]},\"code\":0,\"status\":0,\"type\":\"get_dialogs\"}";
        JSONObject o = JSON.parseObject(json);
        Response r = o.toJavaObject(Response.class);
       // List<?> list = JSON.parseObject(r.getBody().toString(), List.class);
        System.out.println(o.getString("body"));


    }
    public static Request auth() {
        UserPasswordData data = new UserPasswordData();
        data.setLogin(login);
        data.setPassword(password);
        return new Request().setMethod(Methods.LOGIN).setBody(data.toJSONObject());
    }
    public static Request reg(String login, String mail) {
        RegistrationData data = new RegistrationData();
        data.setLogin(login);
        data.setPassword("123456");
        data.setName("Nikita");
        data.setEmail(mail);
        data.setInfo("123456");
        return new Request().setMethod(Methods.REGISTRATION).setBody(data.toJSONObject());
    }
    public static void connect()
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
        CreateDialogRequest dialogRequest = new CreateDialogRequest();
        dialogRequest.setInitialMessage(initMsg);
        dialogRequest.setPartner(partner);
        r.setMethod(Methods.DIALOG_CREATION).setBody(dialogRequest.toJSONObject());
        return r;
    }
    public static Request sendMessage(int dialogId, String text)
    {
        Request r = new Request();
        Message m = new Message();
        m.setDialogId(dialogId);
        m.setText(text);
        r.setMethod(Methods.SEND_MESSAGE).setBody(m.toJSONObject());
        return r;
    }
    static Request getDialogs(int count)
    {
        DialogListRequest request = new DialogListRequest();
        request.setCount("10");
        return new Request().setMethod(Methods.GET_DIALOGS).setBody(request.toJSONObject());
    }
}
