package client.network.queries;

import client.network.ClientSocket;
import client.utils.ApplicationBank;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.ReadMessages;

import java.io.IOException;

public class ReadMessageQuery {
    public static void sendQuery(int dialogId) throws IOException {
        ReadMessages m = new ReadMessages();
        m.setDialogId(dialogId);
        Request r = new Request().setMethod(Methods.READ_MESSAGES).setBody(m.toJSONObject());
        ClientSocket.getInstance().send(r);
    }
    public static void onHandle(Response r)
    {
        if (r.getStatus() == Response.OK)
        {
            ReadMessages m = JSON.parseObject(r.getBody().toJSONString(), ReadMessages.class);
            ApplicationBank.getInstance().getDialogById(m.getDialogId()).readMessages();
        }
    }
}
