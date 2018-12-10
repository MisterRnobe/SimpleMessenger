package server.core;

import common.Methods;
import common.Response;
import common.objects.*;
import common.objects.requests.CreateGroupRequest;
import server.OnlineManager;
import server.database.DatabaseExtractor;
import server.database.DatabaseManager;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CreateChannelHandler extends AbstractHandler<CreateGroupRequest> {
    private Message messageToUsersInChannel = null;
    private List<User> usersInChannel = null;

    public CreateChannelHandler(String login) {
        super(CreateGroupRequest.class, new String[]{"title", "partners"}, login);
    }

    @Override
    protected Body onHandle(CreateGroupRequest body) throws HandleError, SQLException {
        DatabaseExtractor extractor = DatabaseManager.getExtractor();
        int dialogId = extractor.createChannel(login, body.getTitle(), body.getPartners(), body.getAvatar() != null);
        int messageId = extractor.addMessage(dialogId, null, "Created!", System.currentTimeMillis());
        extractor.setLastMessage(dialogId, messageId);
        FullDialog fullDialog = extractor.getFullDialog(dialogId, login);
        List<Message> messages = fullDialog.getDialog().getMessages();
        messageToUsersInChannel = messages.get(messages.size() - 1);
        usersInChannel = extractor.getUsersInDialog(dialogId);
        return fullDialog;
    }

    @Override
    protected void beforeSend() {
        AbstractHandler.sendAllMessage(messageToUsersInChannel, usersInChannel);
    }
}
