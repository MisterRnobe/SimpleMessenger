package server.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Errors;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.User;
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
import java.util.stream.Collectors;

public class EventSocket extends WebSocketAdapter {
    private String login;
    private final Map<String, Function<Request, Response>> handlers = new HashMap<>();

    //Handlers
    private SendMessageHandler messageHandler;
    private CreateDialogHandler createDialogHandler;


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
        List<String> users = DatabaseConnector.getInstance().getUsersInDialog(r.getBody().getString("dialogId")).
                stream().map(User::getLogin).collect(Collectors.toList());
        if (users.size() == 0)
        {
            System.out.println("Нет пользователей в диалоге (Какая - то ошибка)!");
            return r;
        }
        users.remove(this.login);
        OnlineManager.getInstance().sendAll(r, users);
        return r;

    }
    private Response onCreateDialog(Request request, AbstractHandler handler)
    {
        Response r = handler.handle(request);
        if (r.getStatus() == Response.OK)
        {
            List<String> users = r.getBody().getJSONObject("dialogInfo")
                    .getJSONArray("users")
                    .stream()
                    .map(o-> ((JSONObject)o).getString("login"))
                    .filter(s->!s.equalsIgnoreCase(login))
                    .collect(Collectors.toList());
            Response response = new Response();
            response.setStatus(r.getStatus());
            response.setType(r.getType());
            response.setBody(r.getBody().getJSONObject("dialogInfo"));
            OnlineManager.getInstance().sendAll(response, users);
        }
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
        handlers.put(Methods.REGISTER, r->new RegistrationHandler().handle(r));
        handlers.put(Methods.LOGIN, this::onLogin);
    }
    private void onLogin(String login)
    {
        handlers.clear();
        this.messageHandler = new SendMessageHandler(login);
        this.createDialogHandler = new CreateDialogHandler(login);
        handlers.put(Methods.SEND_MESSAGE, this::onSendMessage);
        handlers.put(Methods.CREATE_DIALOG, r-> this.onCreateDialog(r, createDialogHandler));
        handlers.put(Methods.CREATE_GROUP, r-> this.onCreateDialog(r, new CreateGroupHandler(login)));
        handlers.put(Methods.GET_DIALOGS, r -> new GetDialogsHandler(login).handle(r));
        handlers.put(Methods.GET_DIALOG, r -> new GetDialogHandler(login).handle(r));
        handlers.put(Methods.GET_USER_STATUS, r-> new GetUserStatusHandler().handle(r));
        handlers.put(Methods.FIND_USERS, r-> new FindUsersHandler().handle(r));
    }
}
