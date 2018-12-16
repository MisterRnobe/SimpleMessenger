package client.network.queries;

import client.network.ClientSocket;
import client.suppliers.DialogManager;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.requests.DialogIdRequest;

import java.io.IOException;

public class GetDialogInfoQuery {
    public static void send(int dialogId) throws IOException {
        Request r = new Request();
        r.setMethod(Methods.GET_DIALOG_INFO);
        r.setBody(new DialogIdRequest().setDialogId(dialogId).toJSONObject());
        ClientSocket.getInstance().send(r);
    }
    public static void onHandle(Response r){
        if (r.getStatus() == Response.OK){
            DialogManager.getInstance().addDialog(r.getBody());
        }
    }
}
