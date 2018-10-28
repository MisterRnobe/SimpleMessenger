package server.core;

import common.Methods;
import common.Response;
import common.objects.Body;
import common.objects.User;
import server.OnlineManager;
import server.database.DatabaseManager;
import server.servlet.EventSocket;

import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class HookUserStatusHandler extends AbstractHandler<User> {
    private EventSocket socket;

    private Map<String, Consumer<Boolean>> listeners = new TreeMap<>();

    public HookUserStatusHandler(EventSocket socket) {
        super(User.class, new String[]{"login"}, null);
        this.socket = socket;
        socket.addOnCloseListener(()-> OnlineManager.getInstance().removeListeners(listeners));
    }

    @Override
    protected Body onHandle(User body) throws HandleError, SQLException {
        User u = DatabaseManager.getExtractor().getUserStatus(body.getLogin());//DatabaseConnectorOld.getInstance().getUserStatus(body.getLogin());
        u.setOnline(OnlineManager.getInstance().isOnline(body.getLogin()));
        u.setLogin(body.getLogin());
        //UserStatusHook.getInstance().addHook(body.getLogin(), new Listener(socket, body.getLogin()));
        Listener l = new Listener(socket, body.getLogin());
        OnlineManager.getInstance().addListener(body.getLogin(), l);
        listeners.put(body.getLogin(), l);
        return u;
    }

    @Override
    protected void beforeSend(Response response) {
        response.setType(Methods.GET_USER_STATUS);
    }

    private static class Listener implements Consumer<Boolean> {
        private String login;
        private EventSocket socket;
        Listener(EventSocket socket, String login)
        {
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
                user.setOnline(bool);
                user.setLogin(login);
                r.setBody(user.toJSONObject());
                socket.send(r);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
