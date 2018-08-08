package client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.Message;
import common.objects.requests.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.LinkedList;
import java.util.concurrent.Future;

public class Starter {
    public static final String login = "nikita_medvedev";
    public static final String password = "123456";
    private static Session session;
    public static void main(String[] args) {
        CreateGroupRequest r = new CreateGroupRequest();
        r.setTitle("abc");
        r.setPartners(new LinkedList<>());
        r.addPartner("loh");
        r.addPartner("pidor");
        JSONObject o = r.toJSONObject();
        System.out.println(o.toJSONString());


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
        return new Request().setMethod(Methods.REGISTER).setBody(data.toJSONObject());
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
        r.setMethod(Methods.CREATE_DIALOG).setBody(dialogRequest.toJSONObject());
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
