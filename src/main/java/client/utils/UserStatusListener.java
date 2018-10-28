package client.utils;

import common.objects.User;

import java.util.*;
import java.util.function.Consumer;

public class UserStatusListener {
    private static final UserStatusListener instance = new UserStatusListener();
    //private final Map<String, List<Consumer<User>>> handlers = new TreeMap<>();
    private final CallbackMap<User> handlers = new CallbackMap<>();
    private UserStatusListener()
    {}
    public void acceptUser(User u)
    {
        List<Consumer<User>> l = handlers.get(u.getLogin());
        if (l != null)
            l.forEach(c->c.accept(u));
    }
    public void addHandler(String login, Consumer<User> handler)
    {
        List<Consumer<User>> list;
        if ( (list = handlers.get(login)) != null)
        {
            list.add(handler);
        }
        else
        {
            handlers.put(login, handler);
        }
    }
    public boolean exist(String login)
    {
        return handlers.get(login) != null;
    }
    public static UserStatusListener getInstance() {
        return instance;
    }
}
