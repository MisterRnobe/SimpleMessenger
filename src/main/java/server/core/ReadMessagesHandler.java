package server.core;

import common.objects.Body;
import common.objects.ReadMessages;
import server.database.DatabaseManager;

import java.sql.SQLException;

public class ReadMessagesHandler extends AbstractHandler<ReadMessages> {
    public ReadMessagesHandler(String login) {
        super(ReadMessages.class, new String[]{"dialogId"}, login);
    }

    @Override
    protected Body onHandle(ReadMessages body) throws HandleError, SQLException {
        DatabaseManager.getExtractor().readMessages(body.getDialogId(), login);
        return body;
    }
}
