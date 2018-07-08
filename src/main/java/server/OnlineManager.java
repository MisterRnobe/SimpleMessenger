package server;

import server.servlet.EventSocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
