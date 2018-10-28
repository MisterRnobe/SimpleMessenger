package server.core;

import common.objects.Body;
import common.objects.Message;
import server.database.DatabaseExtractor;
import server.database.DatabaseManager;

import java.sql.SQLException;

public class SendMessageHandler extends AbstractHandler<Message>{
    public SendMessageHandler(String login) {
        super(Message.class, new String[]{"dialogId", "text"}, login);
    }

    @Override
    protected Body onHandle(Message body) throws HandleError, SQLException {
        long time = System.currentTimeMillis();
        DatabaseExtractor extractor = DatabaseManager.getExtractor();
        int messageId = extractor.addMessage(body.getDialogId(), login, body.getText(), time);
        //DatabaseConnectorOld.getInstance().setLastMessage(body.getDialogId(), messageId);
        extractor.setLastMessage(body.getDialogId(), messageId);
        body.setSender(login);
        body.setMessageId(messageId);
        body.setTime(time);
        return body;
    }
}
