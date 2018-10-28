package server.core;

import common.objects.Body;
import common.objects.requests.DialogRequest;
import server.database.DatabaseManager;

import java.sql.SQLException;

public class GetDialogHandler extends AbstractHandler<DialogRequest>{
    public GetDialogHandler(String login) {
        super(DialogRequest.class, new String[]{"dialogId"}, login);
    }

    @Override
    protected Body onHandle(DialogRequest body) throws HandleError, SQLException {
        return DatabaseManager.getExtractor().getDialogById(body.getDialogId());
    }
}
