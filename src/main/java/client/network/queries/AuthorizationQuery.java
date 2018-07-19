package client.network.queries;

import client.ApplicationStarter;
import client.CurrentUserBank;
import client.network.ClientSocket;
import client.network.SocketConnection;
import client.window.main.MainWindow;
import client.window.main.MainWindowStarter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.requests.UserPasswordData;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class AuthorizationQuery {

    public static void sendLoginAndPassword(String login, String password) throws IOException {

        UserPasswordData data = new UserPasswordData();
        data.setPassword(password);
        data.setLogin(login);
        Request r = new Request().setMethod(Methods.LOGIN).setBody(data.toJSONObject());
        ClientSocket.getInstance().send(r);
    }
    public static void onHandle(Response r)
    {
        if (r.getStatus() == Response.OK)
        {
            MainWindowStarter.start();
        }
        else
        {

        }
    }
}
