package client.network.queries;

import client.network.ClientSocket;
import client.suppliers.UserSupplier;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.requests.UserPasswordData;

import java.io.IOException;
import java.util.function.Consumer;

public class AuthorizationQuery {
    private static Runnable onSuccess = ()->{};
    private static Consumer<Integer> onFail = (i)->{};

    public static void sendLoginAndPassword(String login, String password, Runnable onSuccess, Consumer<Integer> onFail) throws IOException {

        UserPasswordData data = new UserPasswordData();
        data.setPassword(password);
        data.setLogin(login);
        Request r = new Request().setMethod(Methods.LOGIN).setBody(data.toJSONObject());
        UserSupplier.getInstance().setMyLogin(login);
        ClientSocket.getInstance().send(r);
        AuthorizationQuery.onSuccess = onSuccess;
        AuthorizationQuery.onFail = onFail;
    }
    public static void onHandle(Response r)
    {
        if (r.getStatus() == Response.OK)
        {
            onSuccess.run();
        }
        else
        {
            onFail.accept(r.getCode());
        }
    }
}
