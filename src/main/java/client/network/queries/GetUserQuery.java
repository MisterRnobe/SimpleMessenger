package client.network.queries;

import client.network.ClientSocket;
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
    private static final Map<String, List<Consumer<User>>> handlersMap = new TreeMap<>();
    private static final Set<String> sent = new TreeSet<>();
    public static void requireUser(String login, Consumer<User> handler) throws IOException
    {
        if (!sent.contains(login)) {
            UserRequest userRequest = new UserRequest();
            userRequest.setLogin(login);
            Request r = new Request().setMethod(Methods.GET_USER).setBody(userRequest.toJSONObject());
            ClientSocket.getInstance().send(r);
            sent.add(login);
            handlersMap.put(login, Arrays.asList(handler));
        }
        else
            handlersMap.get(login).add(handler);
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK)
        {
            User user = JSON.parseObject(response.getBody().toJSONString(), User.class);
            handlersMap.get(user.getLogin()).forEach(userConsumer -> userConsumer.accept(user));
            handlersMap.remove(user.getLogin());
            sent.remove(user.getLogin());
        }
    }
}
