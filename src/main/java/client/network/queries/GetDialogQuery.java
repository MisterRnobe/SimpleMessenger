package client.network.queries;

import client.suppliers.DialogManager;
import client.network.ClientSocket;
import client.app.main.MainWindowManager;
import client.utils.CallbackMap;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.Dialog;
import common.objects.requests.DialogRequest;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class GetDialogQuery {

    private static CallbackMap<Dialog> dialogCallbackMap = new CallbackMap<>();

    public static void sendQuery(int dialogId, Consumer<Dialog> handler) throws IOException {
        DialogRequest request = new DialogRequest();
        request.setDialogId(dialogId);
        Request r = new Request().setMethod(Methods.GET_DIALOG).setBody(request.toJSONObject());
        ClientSocket.getInstance().send(r);
        dialogCallbackMap.put(Integer.toString(dialogId), handler);
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK) {
            Dialog dialog =
                    JSON.parseObject(response.getBody().toJSONString(), Dialog.class);
            DialogManager.getInstance().addMessages(dialog.getDialogId(), dialog.getMessages());

            String id = Integer.toString(dialog.getDialogId());
            List<Consumer<Dialog>> consumers = dialogCallbackMap.get(id);
            if (consumers != null){
                consumers.forEach(c->c.accept(dialog));
            }
            dialogCallbackMap.remove(id);
        }
    }
}
