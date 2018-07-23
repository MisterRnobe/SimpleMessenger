package client.network.queries;

import client.application.ApplicationBank;
import client.network.ClientSocket;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.Message;

import java.io.IOException;

public class SendMessageQuery {
    public static void sendQuery(int dialogId, String text) throws IOException {
        Message m = new Message();
        m.setDialogId(dialogId);
        m.setText(text);
        Request r = new Request().setMethod(Methods.SEND_MESSAGE).setBody(m.toJSONObject());
        ClientSocket.getInstance().send(r);
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK) {
            Message m =
                    JSON.parseObject(response.getBody().toJSONString(), Message.class);
            ApplicationBank.getInstance().addMessage(m);
        }

    }
}
