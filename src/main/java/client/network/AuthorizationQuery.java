package client.network;

import client.ApplicationStarter;
import client.CurrentUserBank;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class AuthorizationQuery {
    public static final String url = "http://localhost:200/login/";
    public static void sendLoginAndPassword(String login, String password) throws InterruptedException, ExecutionException, TimeoutException {

        HttpClient httpClient = ApplicationStarter.httpClient;
        ContentResponse response = httpClient.POST(url)
                .param("login", login)
                .param("password", password)
                .send();
        System.out.println(response.getContentAsString());
        JSONObject o = JSON.parseObject(response.getContentAsString());
        String token = o.getJSONObject("body").getString("token");//.get("token");
        CurrentUserBank.getInstance().setToken(token);
    }
}
