package server.core;

import com.alibaba.fastjson.JSONObject;
import common.Errors;
import common.Request;
import common.Response;
import common.objects.Body;
import common.objects.Dialog;
import common.objects.requests.CreateDialogRequest;
import common.objects.requests.DialogListRequest;
import server.DatabaseConnector;
import server.OnlineManager;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CreateDialogHandler extends AbstractHandler<CreateDialogRequest> {


    public CreateDialogHandler(String login) {
        super(CreateDialogRequest.class, new String[]{"partner", "initialMessage"}, login);
    }

    @Override
    protected Body onHandle(CreateDialogRequest body) throws HandleError {
        String creator = login,
                partner = body.getPartner(),
                initialMessage = body.getInitialMessage();

        int dialogId = DatabaseConnector.getInstance().createDialog(creator, partner);
        if (dialogId == -1)
        {
            throw new HandleError(Errors.INTERNAL_ERROR);
        }
        else
        {
            int messageId = DatabaseConnector.getInstance().addMessage(dialogId, creator, initialMessage, System.currentTimeMillis());
            DatabaseConnector.getInstance().setLastMessage(dialogId, messageId);
            return DatabaseConnector.getInstance().getFullDialog(dialogId);
        }
    }

    @Override
    protected void beforeSend(final Response response) {
//        if (response.getStatus() == Response.OK)
//        {
//            List<String> users = response.getBody().getJSONObject("dialogInfo")
//                    .getJSONArray("users")
//                    .stream()
//                    .map(o-> ((JSONObject)o).getString("login"))
//                    .filter(s->!s.equalsIgnoreCase(login))
//                    .collect(Collectors.toList());
//            Response r = new Response();
//            r.setStatus(response.getStatus());
//            r.setType(response.getType());
//            r.setBody(response.getBody().getJSONObject("dialogInfo"));
//            OnlineManager.getInstance().sendAll(r, users);
//        }
    }
}
