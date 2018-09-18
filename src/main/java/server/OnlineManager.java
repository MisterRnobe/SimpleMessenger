package server;

import common.Response;
import server.servlet.EventSocket;

import java.util.*;
import java.util.function.Consumer;

public class OnlineManager {
    private static final OnlineManager instance = new OnlineManager();

    private final Map<String, EventSocket> online = new HashMap<>();
    private final Map<String, List<Consumer<Boolean>>> eventListeners = new TreeMap<>();
    //private final List<Consumer<String>> eventListeners = new LinkedList<>();
    //private final List<Consumer<String>> onLeaveListeners = new LinkedList<>();

    public static OnlineManager getInstance() {
        return instance;
    }

    private OnlineManager(){}

    public void setOnline(String login, EventSocket socket)
    {
        online.put(login, socket);
        DatabaseConnector.getInstance().setOnline(login);
        List<Consumer<Boolean>> l = eventListeners.get(login);
        if (l != null)
            l.forEach(c->c.accept(true));
    }
    public void setOffline(String login)
    {
        List<Consumer<Boolean>> l = eventListeners.get(login);
        if (l != null)
            l.forEach(c->c.accept(false));
        online.remove(login);
        DatabaseConnector.getInstance().setOffline(login);
    }

    public void sendAll(Response response, Collection<String> logins)
    {
        logins.stream().map(online::get).filter(Objects::nonNull).forEach(e->e.send(response));
    }
    public boolean isOnline(String login)
    {
        return online.get(login) != null;
    }

    public void addListener(String login, Consumer<Boolean> listener)
    {
        List<Consumer<Boolean>> list = eventListeners.get(login);
        if (list != null)
            list.add(listener);
        else {
            list = new LinkedList<>();
            list.add(listener);
            eventListeners.put(login, list);
        }
    }

    public void removeListeners(Map<String, Consumer<Boolean>> listeners)
    {
        listeners.forEach((k,v) -> eventListeners.get(k).remove(v));
    }

}

