package client.network.queries;

import client.application.ApplicationBank;
import client.network.ClientSocket;
import client.window.main.MainWindowManager;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.DialogInfo;
import common.objects.FullDialog;
import common.objects.requests.CreateDialogRequest;
import common.objects.requests.CreateGroupRequest;

import java.io.IOException;
import java.util.List;

public class CreateDialogQuery {
    private static int sent = 0;
    public static void sendDialogQuery(String login, String initialMessage) throws IOException {
        CreateDialogRequest r = new CreateDialogRequest();
        r.setPartner(login);
        r.setInitialMessage(initialMessage);
        ClientSocket.getInstance().send(new Request().setMethod(Methods.CREATE_DIALOG).setBody(r.toJSONObject()));
        sent++;
    }
    public static void sendGroupQuery(String title, List<String> partners) throws IOException {
        CreateGroupRequest r = new CreateGroupRequest();
        r.setTitle(title);
        r.setPartners(partners);
        ClientSocket.getInstance().send(new Request().setMethod(Methods.CREATE_GROUP).setBody(r.toJSONObject()));
        sent++;
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK) {
            if (sent != 0) {
                FullDialog fullDialog =
                        JSON.parseObject(response.getBody().toJSONString(), FullDialog.class);
                ApplicationBank.getInstance().addDialogInfo(fullDialog.getDialogInfo());
                ApplicationBank.getInstance().addDialog(fullDialog.getDialog());
                MainWindowManager.getInstance().setDialog(fullDialog.getDialogInfo().getDialogId());
                sent--;
            }
            else
            {
                DialogInfo dialogInfo = JSON.parseObject(response.getBody().toJSONString(), DialogInfo.class);
                ApplicationBank.getInstance().addDialogInfo(dialogInfo);
            }
        }
    }
}
