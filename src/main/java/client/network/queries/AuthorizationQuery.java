package client.network.queries;

import client.utils.ApplicationBank;
import client.network.ClientSocket;
import client.app.main.MainWindowManager;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.requests.UserPasswordData;

import java.io.IOException;

public class AuthorizationQuery {

    public static void sendLoginAndPassword(String login, String password) throws IOException {

        UserPasswordData data = new UserPasswordData();
        data.setPassword(password);
        data.setLogin(login);
        Request r = new Request().setMethod(Methods.LOGIN).setBody(data.toJSONObject());
        ApplicationBank.getInstance().setLogin(login);
        ClientSocket.getInstance().send(r);
    }
    public static void onHandle(Response r)
    {
        if (r.getStatus() == Response.OK)
        {
            MainWindowManager.start();
        }
        else
        {

        }
    }
}
