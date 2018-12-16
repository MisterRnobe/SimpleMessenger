package client.network.queries;

import client.network.ClientSocket;
import client.utils.UserStatusListener;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.User;

import java.io.IOException;
import java.util.function.Consumer;

public class GetUserStatusQuery {
    public static void hookToUser(String login, Consumer<User> handler) throws IOException {
        if (!UserStatusListener.getInstance().exist(login))
        {
            UserStatusListener.getInstance().addHandler(login, handler);
            sendQuery(login);
        }
        else
            UserStatusListener.getInstance().addHandler(login, handler);
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK) {
            User user =
                    JSON.parseObject(response.getBody().toJSONString(), User.class);
            //ApplicationBank.getInstance().updateUserStatus(UserDB);
            UserStatusListener.getInstance().acceptUser(user);
        }
    }
    private static void sendQuery(String login) throws IOException
    {
        User user = new User();
        user.setLogin(login);
        Request r = new Request().setMethod(Methods.HOOK_USER_STATUS).setBody(user.toJSONObject());
        ClientSocket.getInstance().send(r);
    }
}
