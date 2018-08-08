package client.network.queries;

import client.application.ApplicationBank;
import client.network.ClientSocket;
import client.window.main.MainWindowManager;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.Dialog;
import common.objects.UserList;
import common.objects.requests.DialogRequest;
import common.objects.requests.FindUsersRequest;

import java.io.IOException;
import java.util.function.Consumer;

public class FindUsersQuery {
    private static Consumer<UserList> callback;
    public static void sendQuery(String mask, Consumer<UserList> callback) throws IOException {
        FindUsersRequest request = new FindUsersRequest();
        request.setMask(mask);
        ClientSocket.getInstance().send(new Request().setMethod(Methods.FIND_USERS).setBody(request.toJSONObject()));
        FindUsersQuery.callback = callback;
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK) {
            UserList userList =
                    JSON.parseObject(response.getBody().toJSONString(), UserList.class);
            callback.accept(userList);
        }

    }
}
