package server.core;

import common.objects.Body;
import common.objects.requests.CreateGroupRequest;
import server.database.DatabaseExtractor;
import server.database.DatabaseManager;

import java.sql.SQLException;

public class CreateGroupHandler extends AbstractHandler<CreateGroupRequest> {

    public CreateGroupHandler(String login) {
        super(CreateGroupRequest.class, new String[]{"title", "partners"}, login);
    }

    @Override
    protected Body onHandle(CreateGroupRequest body) throws HandleError, SQLException {
        DatabaseExtractor extractor = DatabaseManager.getExtractor();
        int dialogId = extractor.createGroup(login, body.getTitle(), body.getPartners());
        int messageId = extractor.addMessage(dialogId, null, "Created!", System.currentTimeMillis());
        extractor.setLastMessage(dialogId, messageId);
        return extractor.getFullDialog(dialogId);
    }
}
