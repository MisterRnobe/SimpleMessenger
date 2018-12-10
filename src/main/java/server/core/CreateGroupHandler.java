package server.core;

import common.Methods;
import common.Response;
import common.objects.*;
import common.objects.requests.CreateGroupRequest;
import server.OnlineManager;
import server.database.DatabaseExtractor;
import server.database.DatabaseManager;
import server.utils.FileSaver;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CreateGroupHandler extends AbstractHandler<CreateGroupRequest> {
    private List<User> usersFromLastGroup;
    private Message messageToLastCreatedGroup;

    public CreateGroupHandler(String login) {
        super(CreateGroupRequest.class, new String[]{"title", "partners"}, login);
    }

    @Override
    protected Body onHandle(CreateGroupRequest body) throws HandleError, SQLException {
        DatabaseExtractor extractor = DatabaseManager.getExtractor();
        int dialogId = extractor.createGroup(login, body.getTitle(), body.getPartners(), body.getAvatar() != null);
        if (body.getAvatar() != null)
            FileSaver.saveGroupAvatar(body.getAvatar(), dialogId);
        int messageId = extractor.addMessage(dialogId, null, "Created!", System.currentTimeMillis());
        extractor.setLastMessage(dialogId, messageId);
        FullDialog fullDialog = extractor.getFullDialog(dialogId, login);
        usersFromLastGroup = extractor.getUsersInDialog(dialogId);
        List<Message> messages = fullDialog.getDialog().getMessages();
        messageToLastCreatedGroup = messages.get(messages.size() - 1);

        return fullDialog;
    }

    @Override
    protected void beforeSend() {
        AbstractHandler.sendAllMessage(messageToLastCreatedGroup, usersFromLastGroup);
    }
}
