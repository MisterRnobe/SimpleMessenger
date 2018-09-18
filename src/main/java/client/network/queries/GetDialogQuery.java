package client.network.queries;

import client.utils.ApplicationBank;
import client.network.ClientSocket;
import client.app.main.MainWindowManager;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.Dialog;
import common.objects.requests.DialogRequest;

import java.io.IOException;

public class GetDialogQuery {
    public static void sendQuery(int dialogId) throws IOException {
        DialogRequest request = new DialogRequest();
        request.setDialogId(dialogId);
        Request r = new Request().setMethod(Methods.GET_DIALOG).setBody(request.toJSONObject());
        ClientSocket.getInstance().send(r);
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK) {
            Dialog dialog =
                    JSON.parseObject(response.getBody().toJSONString(), Dialog.class);
            ApplicationBank.getInstance().addDialog(dialog);
            MainWindowManager.getInstance().setDialog(dialog.getDialogId());
        }

    }
}
