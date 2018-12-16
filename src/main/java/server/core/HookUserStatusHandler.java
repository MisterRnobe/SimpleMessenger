package server.core;

import common.Methods;
import common.Response;
import common.objects.Body;
import common.objects.User;
import server.OnlineManager;
import server.database.DatabaseManager;
import server.servlet.EventSocket;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class HookUserStatusHandler extends AbstractHandler<User> {
    private EventSocket socket;
    private String lastBoundLogin;

    private Map<String, Consumer<Boolean>> listeners = new TreeMap<>();

    public HookUserStatusHandler(EventSocket socket, String login) {
        super(User.class, new String[]{"login"}, login);
        this.socket = socket;
        socket.addOnCloseListener(() -> OnlineManager.getInstance().removeListeners(listeners));
    }

    @Override
    protected Body onHandle(User body) throws HandleError {
        Listener l = new Listener(socket, body.getLogin());
        OnlineManager.getInstance().addListener(body.getLogin(), l);
        listeners.put(body.getLogin(), l);
        lastBoundLogin = body.getLogin();
        return Body.NULL_BODY;
    }

    @Override
    protected void beforeSend() {
        Response response = new Response();
        response.setStatus(Response.OK);
        response.setType(Methods.GET_USER_STATUS);
        User u = null;
        try {
            u = DatabaseManager.getExtractor().getUserStatus(lastBoundLogin);
            u.setIsOnline(OnlineManager.getInstance().isOnline(lastBoundLogin));
            u.setLogin(lastBoundLogin);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (u!= null) {
            response.setBody(u.toJSONObject());
        }
        OnlineManager.getInstance().sendAll(response, Arrays.asList(login));
    }

    private static class Listener implements Consumer<Boolean> {
        private String login;
        private EventSocket socket;

        Listener(EventSocket socket, String login) {
            this.socket = socket;
            this.login = login;
        }

        @Override
        public void accept(Boolean bool) {
            Response r = new Response();
            r.setStatus(Response.OK);
            r.setType(Methods.GET_USER_STATUS);
            try {
                User user = DatabaseManager.getExtractor().getUserStatus(login);
                user.setIsOnline(bool);
                user.setLogin(login);
                r.setBody(user.toJSONObject());
                socket.send(r);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
