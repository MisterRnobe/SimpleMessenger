package server.core;

import common.Errors;
import common.objects.Body;
import common.objects.requests.CreateGroupRequest;
import server.DatabaseConnector;

public class CreateChannelHandler extends AbstractHandler<CreateGroupRequest>{
    public CreateChannelHandler(String login) {
        super(CreateGroupRequest.class, new String[]{"title", "partners"}, login);
    }

    @Override
    protected Body onHandle(CreateGroupRequest body) throws HandleError {
        int dialogId = DatabaseConnector.getInstance().createChannel(login, body.getTitle(), body.getPartners());
        if (dialogId == -1)
            throw new HandleError(Errors.INTERNAL_ERROR);
        else
        {
            int messageId = DatabaseConnector.getInstance().addMessage(dialogId, null, "Created!", System.currentTimeMillis());
            DatabaseConnector.getInstance().setLastMessage(dialogId, messageId);
            return DatabaseConnector.getInstance().getFullDialog(dialogId);
        }
    }
}
