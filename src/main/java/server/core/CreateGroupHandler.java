package server.core;

import com.alibaba.fastjson.JSONObject;
import common.Response;
import common.objects.Body;
import common.objects.DialogMarker;
import common.objects.FullDialog;
import common.objects.User;
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
    private DialogMarker lastDialog;

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
        lastDialog = fullDialog.getInfo();

        return fullDialog;
    }

    @Override
    protected void beforeSend(Response response) {
        Response newGroupMessage = new Response();
        newGroupMessage.setStatus(Response.OK);
        newGroupMessage.setType(response.getType());
        newGroupMessage.setBody(lastDialog.toJSONObject());
        OnlineManager.getInstance().sendAll(newGroupMessage,
                usersFromLastGroup.stream().map(User::getLogin).filter(str->!str.equalsIgnoreCase(login))
                .collect(Collectors.toList()));
    }
}
