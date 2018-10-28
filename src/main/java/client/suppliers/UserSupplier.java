package client.suppliers;

import client.network.queries.GetUserQuery;
import client.utils.CallbackMap;
import common.objects.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class UserSupplier {
    private static UserSupplier instance;
    public static UserSupplier getInstance()
    {
        if (instance == null)
            instance = new UserSupplier();
        return instance;
    }
    private String myLogin;
    private Map<String, User> userMap = new TreeMap<>();
    private CallbackMap<User> callbackMap = new CallbackMap<>();

    private UserSupplier(){}

    public String getMyLogin() {
        return myLogin;
    }
    @Deprecated
    public User getUserByLogin(String login)
    {
        return userMap.get(login);
    }
    private void addUser(User user)
    {
        this.userMap.put(user.getLogin(), user);
        List<Consumer<User>> list = callbackMap.get(user.getLogin());
        if (list != null)
        {
            list.forEach(c->c.accept(user));
            callbackMap.remove(user.getLogin());
        }
    }

    public UserSupplier setMyLogin(String myLogin) {
        this.myLogin = myLogin;
        return this;
    }
    public List<User> getFriendList()
    {
        return DialogManager.getInstance().getDialogsOnly()
                .stream().map(d->d.partner).collect(Collectors.toList());
    }
    public void supply(String login, Consumer<User> callback)
    {
        User u = userMap.get(login);
        if (u != null) {
            callback.accept(u);
            return;
        }
        callbackMap.put(login, callback);
        try {
            GetUserQuery.requireUser(login, this::addUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
