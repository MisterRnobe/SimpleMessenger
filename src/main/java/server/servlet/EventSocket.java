package server.servlet;

import com.alibaba.fastjson.JSON;
import common.Errors;
import common.Methods;
import common.Request;
import common.Response;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import server.DatabaseConnector;
import server.OnlineManager;
import server.core.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EventSocket extends WebSocketAdapter {
    private String login;
    private final Map<String, Function<Request, Response>> handlers = new HashMap<>();

    //Handlers
    private SendMessageHandler messageHandler;


    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        System.out.println("CONNECTED!");
        onStart();
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
    private Response onLogin(Request request)
    {
        Response r = new LoginHandler().handle(request);
        if (r.getStatus() == Response.OK)
        {
            this.login = request.getBody().getString("login");
            OnlineManager.getInstance().setOnline(login, this);
            onLogin(login);
        }
        return r;
    }
    private Response onSendMessage(Request request)
    {
        Response r = this.messageHandler.handle(request);
        List<String> users = DatabaseConnector.getInstance().getUsersInDialog(r.getBody().getString("dialogId"));
        if (users == null)
        {
            System.out.println("Нет пользователей в диалоге (Какая - то ошибка)!");
            return r;
        }
        users.remove(this.login);
        OnlineManager.getInstance().sendAll(r, users.toArray(new String[0]));
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
    private void onStart()
    {
        handlers.put(Methods.REGISTRATION, r->new RegistrationHandler().handle(r));
        handlers.put(Methods.LOGIN, this::onLogin);
    }
    private void onLogin(String login)
    {
        handlers.clear();
        this.messageHandler = new SendMessageHandler(login);
        handlers.put(Methods.SEND_MESSAGE, this::onSendMessage);
        handlers.put(Methods.DIALOG_CREATION, r -> new CreateDialogHandler(login).handle(r));
        handlers.put(Methods.GET_DIALOGS, r -> new GetDialogsHandler(login).handle(r));
    }
}
