package server.core;

import common.Errors;
import common.Request;
import common.Response;
import common.objects.Body;
import common.objects.Dialog;
import common.objects.requests.CreateDialogRequest;
import common.objects.requests.DialogListRequest;
import server.DatabaseConnector;

import java.util.TreeMap;

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
            int messageId = DatabaseConnector.getInstance().addMessage(Integer.toString(dialogId), creator, initialMessage, Long.toString(System.currentTimeMillis()));
            DatabaseConnector.getInstance().setLastMessage(Integer.toString(dialogId), Integer.toString(messageId));
            return DatabaseConnector.getInstance().getDialogById(Integer.toString(dialogId));
        }
    }
}
