package server.core;

import common.objects.Body;
import common.objects.DialogList;
import common.objects.requests.DialogListRequest;
import server.DatabaseConnector;

public class GetDialogsHandler extends AbstractHandler<DialogListRequest> {
    public GetDialogsHandler(String login) {
        super(DialogListRequest.class, new String[]{"count"}, login);
    }

    @Override
    protected Body onHandle(DialogListRequest body) throws HandleError {
        return DatabaseConnector.getInstance().getDialogs(login, body.getCount());
    }

}
