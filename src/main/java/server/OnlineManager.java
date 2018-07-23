package server;

import common.Response;
import server.servlet.EventSocket;

import java.util.*;

public class OnlineManager {
    private static final OnlineManager instance = new OnlineManager();

    private Map<String, EventSocket> online = new HashMap<>();

    public static OnlineManager getInstance() {
        return instance;
    }

    private OnlineManager(){}

    public void setOnline(String login, EventSocket socket)
    {
        online.put(login, socket);
        DatabaseConnector.getInstance().setOnline(login);
    }
    public void setOffline(String login)
    {
        online.remove(login);
        DatabaseConnector.getInstance().setOffline(login);
    }
    public EventSocket getSocket(String login)
    {
        return online.get(login);
    }
    public void sendAll(Response response, String... logins)
    {
        Arrays.stream(logins).map(s -> online.get(s)).filter(Objects::nonNull).forEach(e->e.send(response));
    }
    public boolean isOnline(String login)
    {
        return online.get(login) != null;
    }
}
