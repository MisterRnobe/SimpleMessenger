package server.core;

import common.objects.*;
import common.objects.requests.CreateGroupRequest;
import server.database.DatabaseExtractor;
import server.database.DatabaseManager;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CreateChannelHandler extends AbstractHandler<CreateGroupRequest> {
    private int dialogId;
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
        this.dialogId = fullDialog.getInfo().getDialogId();
        usersInChannel = extractor.getUsersInDialog(dialogId).stream()
                .filter(u -> !u.getLogin().equals(login))
                .collect(Collectors.toList());
        return fullDialog;
    }

    @Override
    protected void beforeSend() {
        AbstractHandler.sendAllNewDialog(dialogId, usersInChannel);
    }
}
