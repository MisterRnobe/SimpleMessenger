package client.network.queries;

import client.network.ClientSocket;
import client.utils.CallbackMap;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.User;
import common.objects.requests.UserRequest;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class GetUserQuery {
    private static final CallbackMap<User> callbacks = new CallbackMap<>();
    private static final Set<String> sent = new TreeSet<>();
    public static void requireUser(String login, Consumer<User> callback) throws IOException
    {
        if (!sent.contains(login)) {
            UserRequest userRequest = new UserRequest();
            userRequest.setLogin(login);
            Request r = new Request().setMethod(Methods.GET_USER).setBody(userRequest.toJSONObject());
            ClientSocket.getInstance().send(r);
            sent.add(login);
            callbacks.put(login, callback);
        }
        else
            callbacks.get(login).add(callback);
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK)
        {
            User user = JSON.parseObject(response.getBody().toJSONString(), User.class);
            callbacks.get(user.getLogin()).forEach(userConsumer -> userConsumer.accept(user));
            callbacks.remove(user.getLogin());
            sent.remove(user.getLogin());
        }
    }
}
