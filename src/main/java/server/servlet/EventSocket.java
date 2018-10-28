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
import server.OnlineManager;
import server.core.*;
import server.database.DatabaseManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventSocket extends WebSocketAdapter {
    private String login;
    private final Map<String, Function<Request, Response>> handlers = new HashMap<>();

    //Handlers
    private SendMessageHandler messageHandler;

    //
    private List<Runnable> onCloseListeners = new LinkedList<>();

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
    public void onWebSocketError(Throwable cause) {
        System.out.println("ERROR "+ cause.getMessage());
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        onCloseListeners.forEach(Runnable::run);
        onCloseListeners.clear();
        onCloseListeners = null;
        if (login!=null)
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
        List<String> users;
        try {
            users = DatabaseManager.getExtractor().getUsersInDialog(r.getBody().getInteger("dialogId"))//DatabaseConnectorOld.getInstance().getUsersInDialog(r.getBody().getInteger("dialogId")).
                    .stream().map(User::getLogin).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
            return r;
        }
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
    public void addOnCloseListener(Runnable r)
    {
        onCloseListeners.add(r);
    }
    private void onStart()
    {
        handlers.put(Methods.REGISTER, r->new RegistrationHandler().handle(r));
        handlers.put(Methods.LOGIN, this::onLogin);
        handlers.put(Methods.VERIFY_DATA, r->new VerifyDataHandler().handle(r));
    }
    private void onLogin(String login)
    {
        handlers.clear();
        this.messageHandler = new SendMessageHandler(login);

        handlers.put(Methods.SEND_MESSAGE, this::onSendMessage);
        handlers.put(Methods.CREATE_DIALOG, r-> this.onCreateDialog(r, new CreateDialogHandler(login)));
        handlers.put(Methods.CREATE_CHANNEL, r -> this.onCreateDialog(r, new CreateChannelHandler(login)));
        handlers.put(Methods.CREATE_GROUP, r-> this.onCreateDialog(r, new CreateGroupHandler(login)));
        handlers.put(Methods.GET_DIALOGS, r -> new GetDialogsHandler(login).handle(r));
        handlers.put(Methods.GET_DIALOG, r -> new GetDialogHandler(login).handle(r));
        handlers.put(Methods.HOOK_USER_STATUS, r-> new HookUserStatusHandler(this).handle(r));
        handlers.put(Methods.FIND_USERS, r-> new FindUsersHandler().handle(r));
        //handlers.put(Methods.MODIFY_GROUP, r->new AddUsersToGroupHandler(login).handle(r));
        handlers.put(Methods.READ_MESSAGES, r->new ReadMessagesHandler(login).handle(r));
        handlers.put(Methods.GET_PROFILE, r->new UserProfileHandler().handle(r));
        handlers.put(Methods.GET_FILE, r->new FileHandler().handle(r));
        handlers.put(Methods.GET_USER, r->new GetUserHandler().handle(r));
    }
}
