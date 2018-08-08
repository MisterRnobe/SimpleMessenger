package server.core;

import common.Errors;
import common.objects.Body;
import common.objects.requests.CreateGroupRequest;
import server.DatabaseConnector;

public class CreateGroupHandler extends AbstractHandler<CreateGroupRequest> {

    public CreateGroupHandler(String login) {
        super(CreateGroupRequest.class, new String[]{"title", "partners"}, login);
    }

    @Override
    protected Body onHandle(CreateGroupRequest body) throws HandleError {
        int dialogId = DatabaseConnector.getInstance().createGroup(login, body.getTitle(), body.getPartners());
        if (dialogId == -1)
            throw new HandleError(Errors.INTERNAL_ERROR);
        else
        {
            int messageId = DatabaseConnector.getInstance().addMessage(Integer.toString(dialogId), null, "Created!", Long.toString(System.currentTimeMillis()));
            DatabaseConnector.getInstance().setLastMessage(Integer.toString(dialogId), Integer.toString(messageId));
            return DatabaseConnector.getInstance().getFullDialog(Integer.toString(dialogId));
        }
    }
}
