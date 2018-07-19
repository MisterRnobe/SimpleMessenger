package client.network;

import client.network.queries.AuthorizationQuery;
import client.network.queries.GetDialogsQuery;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class ClientSocket extends WebSocketAdapter {
    private static ClientSocket instance;

    private final Map<String, Consumer<Response>> handlers = new HashMap<>();
    {
        handlers.put(Methods.LOGIN, AuthorizationQuery::onHandle);
        handlers.put(Methods.GET_DIALOGS, GetDialogsQuery::onHandle);
    }

    public static ClientSocket getInstance() {
        return instance;
    }
    public void send(Request r) throws IOException {
        String s = JSON.toJSONString(r);
        System.out.println("Sending: "+s);
        getSession().getRemote().sendString(s);
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
}
