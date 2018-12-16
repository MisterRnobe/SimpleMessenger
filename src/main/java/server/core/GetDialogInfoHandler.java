package server.core;

import common.objects.Body;
import common.objects.FullDialog;
import common.objects.requests.DialogIdRequest;
import server.database.DatabaseExtractor;
import server.database.DatabaseManager;

import java.sql.SQLException;

public class GetDialogInfoHandler extends AbstractHandler<DialogIdRequest> {
    public GetDialogInfoHandler(String login) {
        super(DialogIdRequest.class, new String[]{"dialogId"}, login);
    }

    @Override
    protected Body onHandle(DialogIdRequest body) throws HandleError, SQLException {
        DatabaseExtractor extractor = DatabaseManager.getExtractor();

        FullDialog fullDialog = extractor.getFullDialog(body.getDialogId(), login);


        return fullDialog.getInfo();
    }
}
