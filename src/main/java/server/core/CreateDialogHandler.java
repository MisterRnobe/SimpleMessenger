package server.core;

import common.Errors;
import common.objects.*;
import common.objects.requests.CreateDialogRequest;
import server.database.DatabaseExtractor;
import server.database.DatabaseManager;

import java.sql.SQLException;
import java.util.Arrays;

public class CreateDialogHandler extends AbstractHandler<CreateDialogRequest> {
    private User lastPartner;
    private int dialogId;


    public CreateDialogHandler(String login) {
        super(CreateDialogRequest.class, new String[]{"partner", "initialMessage"}, login);
    }

    @Override
    protected Body onHandle(CreateDialogRequest body) throws HandleError, SQLException {
        DatabaseExtractor extractor = DatabaseManager.getExtractor();
        int dialogId = extractor.createDialog(login, body.getPartner());
        if (dialogId == -1) {
            throw new HandleError(Errors.INTERNAL_ERROR);
        }
        int messageId = extractor.addMessage(dialogId, login, body.getInitialMessage(), System.currentTimeMillis());
        extractor.setLastMessage(dialogId, messageId);
        FullDialog fullDialog = extractor.getFullDialog(dialogId, login);
        this.dialogId = fullDialog.getInfo().getDialogId();
        lastPartner = ((DialogInfo) fullDialog.getInfo()).getPartner();

        return fullDialog;

    }

    @Override
    protected void beforeSend() {


        AbstractHandler.sendAllNewDialog(dialogId, Arrays.asList(lastPartner));

    }
}
