package client.network.queries;

import client.network.ClientSocket;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.UserProfile;
import common.objects.requests.UserRequest;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class GetUserProfileQuery {
    private static final Map<String, Consumer<UserProfile>> callbacks = new TreeMap<>();
    public static void getProfile(String login, Consumer<UserProfile> callback) throws IOException
    {
        UserRequest userRequest = new UserRequest();
        userRequest.setLogin(login);
        Request r = new Request().setMethod(Methods.GET_PROFILE).setBody(userRequest.toJSONObject());
        ClientSocket.getInstance().send(r);
        callbacks.put(login, callback);
    }
    public static void handle(Response r)
    {
        if (r.getStatus() == Response.OK)
        {
            UserProfile profile = JSON.parseObject(r.getBody().toJSONString(), UserProfile.class);
            Consumer<UserProfile> callback = callbacks.get(profile.getLogin());
            if (callback != null)
            {
                callback.accept(profile);
                callbacks.remove(profile.getLogin());
            }
        }
    }

}
