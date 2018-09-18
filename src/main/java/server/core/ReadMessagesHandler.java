package server.core;

import common.Errors;
import common.objects.Body;
import common.objects.ReadMessages;
import server.DatabaseConnector;

public class ReadMessagesHandler extends AbstractHandler<ReadMessages> {
    public ReadMessagesHandler(String login) {
        super(ReadMessages.class, new String[]{"dialogId"}, login);
    }

    @Override
    protected Body onHandle(ReadMessages body) throws HandleError {
        boolean ok = DatabaseConnector.getInstance().readMessages(body.getDialogId(), login);
        if (ok)
            return body;
        else
            throw new HandleError(Errors.INTERNAL_ERROR);
    }
}
