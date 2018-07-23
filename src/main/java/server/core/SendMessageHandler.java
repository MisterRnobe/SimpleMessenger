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
        String dialogId = Integer.toString(body.getDialogId()),
                text = body.getText();
        long time = System.currentTimeMillis();
        int messageId = DatabaseConnector.getInstance().addMessage(dialogId, login, text, Long.toString(time));
        DatabaseConnector.getInstance().setLastMessage(dialogId, Integer.toString(messageId));
        body.setSender(login);
        body.setMessageId(messageId);
        body.setTime(time);
        return body;
    }
}
