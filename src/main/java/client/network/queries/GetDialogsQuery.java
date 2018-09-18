package client.network.queries;

import client.utils.ApplicationBank;
import client.network.ClientSocket;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.DialogList;
import common.objects.requests.DialogListRequest;

import java.io.IOException;

public class GetDialogsQuery {
    public static void sendQuery(int count) throws IOException {
        DialogListRequest request = new DialogListRequest();
        request.setCount(Integer.toString(count));
        Request r = new Request().setMethod(Methods.GET_DIALOGS).setBody(request.toJSONObject());
        ClientSocket.getInstance().send(r);
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK) {
            DialogList dialogList =
                    JSON.parseObject(response.getBody().toJSONString(), DialogList.class);
            ApplicationBank.getInstance().addDialogsInfo(dialogList);
        }

    }
}
