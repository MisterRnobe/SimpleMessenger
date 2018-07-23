package client.network.queries;

import client.application.ApplicationBank;
import client.network.ClientSocket;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.DialogList;
import common.objects.User;
import common.objects.requests.DialogListRequest;

import java.io.IOException;

public class GetUserStatusQuery {
    public static void sendQuery(String login) throws IOException {
        User user = new User();
        user.setLogin(login);
        Request r = new Request().setMethod(Methods.GET_USER_STATUS).setBody(user.toJSONObject());
        ClientSocket.getInstance().send(r);
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK) {
            User user =
                    JSON.parseObject(response.getBody().toJSONString(), User.class);
            ApplicationBank.getInstance().updateUserStatus(user);
        }
    }
}
