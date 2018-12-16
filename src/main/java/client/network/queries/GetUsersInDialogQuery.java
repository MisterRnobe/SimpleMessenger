package client.network.queries;

import client.network.ClientSocket;
import client.network.SocketConnection;
import client.utils.CallbackMap;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.UserList;
import common.objects.UserListInDialog;
import common.objects.requests.GetUsersInDialogData;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class GetUsersInDialogQuery {
    private static final CallbackMap<UserListInDialog> callbacks = new CallbackMap<>();

    public static void send(int dialogId, Consumer<UserListInDialog> callback) throws IOException {
        Request r = new Request()
                .setMethod(Methods.GET_USERS_IN_DIALOG)
                .setBody(new GetUsersInDialogData().setDialogId(dialogId).toJSONObject());
        ClientSocket.getInstance().send(r);
        callbacks.put(Integer.toString(dialogId), callback);
    }
    public static void onHandle(Response r){
        if (r.getCode() == Response.OK){
            List<Consumer<UserListInDialog>> consumers = callbacks.get(r.getBody().getString("dialogId"));
            if (consumers!= null){
                UserListInDialog userListInDialog = JSON.parseObject(r.getBody().toJSONString(), UserListInDialog.class);
                consumers.forEach(c->c.accept(userListInDialog));
                callbacks.remove(Integer.toString(userListInDialog.getDialogId()));
            }
        }
    }
}
