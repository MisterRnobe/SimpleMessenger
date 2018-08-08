package client.network.queries;

import client.application.Listener;
import client.network.ClientSocket;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.requests.RegistrationData;

import java.io.IOException;

public class RegistrationQuery {
    private static Listener<Integer> errorListener, successListener;
    public static void sendQuery(String login, String password, String name, String email, String info) throws IOException {

        RegistrationData data = new RegistrationData();
        data.setPassword(password);
        data.setLogin(login);
        data.setName(name);
        data.setEmail(email);
        data.setInfo(info);
        Request r = new Request().setMethod(Methods.REGISTER).setBody(data.toJSONObject());
        ClientSocket.getInstance().send(r);
    }

    public static void setErrorListener(Listener<Integer> errorListener) {
        RegistrationQuery.errorListener = errorListener;
    }

    public static void setSuccessListener(Listener<Integer> successListener) {
        RegistrationQuery.successListener = successListener;
    }

    public static void onHandle(Response r)
    {
        if (r.getStatus() == Response.OK)
        {
            successListener.onHandle(Response.OK);
        }
        else
        {
            errorListener.onHandle(r.getCode());
        }
    }
}
