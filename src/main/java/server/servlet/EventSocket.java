package server.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Errors;
import common.Methods;
import common.Request;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import server.DatabaseConnector;
import server.OnlineManager;
import server.core.Authorization;
import server.core.DialogCreator;
import server.core.Message;
import server.core.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class EventSocket extends WebSocketAdapter {
    private String login;
    private final Map<String, Function<Request, Response>> handlers = new HashMap<>();
    {
        System.out.println("START...");
        handlers.put(Methods.AUTH, this::onAuth);
        handlers.put(Methods.DIALOG_CREATION, DialogCreator::createConversation);
        handlers.put(Methods.SEND_MESSAGE, this::onSendMessage);
    }

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        System.out.println("CONNECTED!");
    }

    @Override
    public void onWebSocketText(String message) {
        System.out.println("MESSAGE: "+message);
        Request request = JSON.parseObject(message, Request.class);
        Function<Request, Response> handler = handlers.get(request.getMethod());
        Response r;
        if (handler != null)
        {
            r = handler.apply(request);
        }
        else
        {
            r = new Response();
            r.setCode(Errors.WRONG_REQUEST_PARAMETERS);
        }
        send(r);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        OnlineManager.getInstance().setOffline(login);
        System.out.println("CLOSED");
    }
    private Response onAuth(Request request)
    {
        Response r = Authorization.auth(request);
        if (r.getStatus() == Response.OK)
        {
            this.login = request.getBody().get("login");
            OnlineManager.getInstance().setOnline(login, this);
        }
        return r;
    }
    private Response onSendMessage(Request request)
    {
        Response r = Message.sendMessage(request);
        List<String> users = DatabaseConnector.getInstance().getUsersInDialog(r.getBody().get("dialog_id"));
        if (users == null)
        {
            System.out.println("Нет пользователей в диалоге (Какая - то ошибка)!");
            return r;
        }
        users.stream().map(s->OnlineManager.getInstance().getSocket(s))
                .filter(socket -> Objects.nonNull(socket) && socket != this)
                .forEach(socket -> socket.send(r));
        return r;

    }
    public void send(Response r)
    {
        try {
            getRemote().sendString(JSON.toJSONString(r));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
