package client.network;

import client.network.queries.*;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.requests.GetFileRequest;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ClientSocket extends WebSocketAdapter {
    private static ClientSocket instance;

    private final Map<String, Consumer<Response>> handlers = new HashMap<>();
    {
        handlers.put(Methods.LOGIN, AuthorizationQuery::onHandle);
        handlers.put(Methods.GET_DIALOGS, GetDialogsQuery::onHandle);
        handlers.put(Methods.GET_DIALOG, GetDialogQuery::onHandle);
        handlers.put(Methods.SEND_MESSAGE, SendMessageQuery::onHandle);
        handlers.put(Methods.GET_USER_STATUS, GetUserStatusQuery::onHandle);
        handlers.put(Methods.REGISTER, RegistrationQuery::onHandle);
        handlers.put(Methods.FIND_USERS, FindUsersQuery::onHandle);
        handlers.put(Methods.CREATE_DIALOG, CreateDialogQuery::onHandle);
        handlers.put(Methods.CREATE_GROUP, CreateDialogQuery::onHandle);
        handlers.put(Methods.CREATE_CHANNEL, CreateDialogQuery::onHandle);
        handlers.put(Methods.MODIFY_GROUP, GroupModificationQuery::onHandle);
        handlers.put(Methods.JOIN_GROUP, GroupModificationQuery::onJoinedGroup);
        handlers.put(Methods.READ_MESSAGES, ReadMessageQuery::onHandle);
        handlers.put(Methods.GET_PROFILE, GetUserProfileQuery::handle);
        handlers.put(Methods.VERIFY_DATA, VerifyDataQuery::onHandle);
        handlers.put(Methods.GET_FILE, GetFileQuery::onHandle);
    }

    public static ClientSocket getInstance() {
        return instance;
    }
    public void send(Request r) throws IOException {
        String s = JSON.toJSONString(r);
        System.out.println("Sending: "+s);
        getSession().getRemote().sendString(s);
        //System.out.println("JUST SENT!");
    }

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        instance = this;
    }

    @Override
    public void onWebSocketText(String message) {
        System.out.println("RECEIVED: "+message);
        Response r = JSON.parseObject(message, Response.class);
        Consumer<Response>  consumer = handlers.get(r.getType());
        if (consumer != null)
            consumer.accept(r);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        System.out.println("ERROR: "+cause.getMessage());
    }
}
