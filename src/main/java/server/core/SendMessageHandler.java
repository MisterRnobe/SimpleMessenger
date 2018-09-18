package server.core;

import common.objects.Body;
import common.objects.Message;
import server.DatabaseConnector;

public class SendMessageHandler extends AbstractHandler<Message>{
    public SendMessageHandler(String login) {
        super(Message.class, new String[]{"dialogId", "text"}, login);
    }

    @Override
    protected Body onHandle(Message body) throws HandleError {
        long time = System.currentTimeMillis();
        int messageId = DatabaseConnector.getInstance().addMessage(body.getDialogId(), login, body.getText(), time);
        DatabaseConnector.getInstance().setLastMessage(body.getDialogId(), messageId);
        body.setSender(login);
        body.setMessageId(messageId);
        body.setTime(time);
        return body;
    }
}
