package server.core;

import common.objects.Body;
import common.objects.requests.DialogRequest;
import server.DatabaseConnector;

public class GetDialogHandler extends AbstractHandler<DialogRequest>{
    public GetDialogHandler(String login) {
        super(DialogRequest.class, new String[]{"dialogId"}, login);
    }

    @Override
    protected Body onHandle(DialogRequest body) throws HandleError {
        return DatabaseConnector.getInstance().getDialogById(body.getDialogId());
    }
}
