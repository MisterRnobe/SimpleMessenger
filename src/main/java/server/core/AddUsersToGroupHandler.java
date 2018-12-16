package server.core;

import common.Errors;
import common.Methods;
import common.Response;
import common.objects.Body;
import common.objects.DialogInfo;
import common.objects.GroupModification;
import common.objects.User;
import common.objects.requests.AddUsersToGroupRequest;
import server.OnlineManager;
import server.database.DatabaseExtractor;
import server.database.DatabaseManager;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

// FIXME: 14.10.2018 Implement GroupModification
@Deprecated
public class AddUsersToGroupHandler extends AbstractHandler<AddUsersToGroupRequest> {
    private List<String> users;
    private GroupModification response;
    public AddUsersToGroupHandler(String login) {
        super(AddUsersToGroupRequest.class, new String[]{"userDBS","dialogId"}, login);
    }

    @Override
    protected Body onHandle(AddUsersToGroupRequest body) throws HandleError, SQLException {
        //DatabaseConnectorOld connector = DatabaseConnectorOld.getInstance();
        throw new HandleError(Errors.INTERNAL_ERROR);
        /*DatabaseExtractor extractor = DatabaseManager.getExtractor();
        userDBS = extractor.getUsersInDialog(body.getDialogId()).stream()
                .map(UserDB::getLogin).collect(Collectors.toList());
        extractor.addUsersToDialog(body.getDialogId(), body.getUserDBS());
        LinkedList<Integer> messageIds = new LinkedList<>();
        for(String u: body.getUserDBS())
        {
            messageIds.add(extractor.addMessage(body.getDialogId(), null, login+" added "+u, System.currentTimeMillis()));
        }
        if (messageIds.size() != 0)
        {
             extractor.setLastMessage(body.getDialogId(), messageIds.getLast());
        }
        response = new GroupModification();
        response.setType(GroupModification.NEW_USERS);
        List<UserDB> userDBS = new LinkedList<>();
        for (String login: body.getUserDBS())
        {
            userDBS.add(extractor.getUser(login));
        }
        //response.setUserDBS(body.getUserDBS().stream().map(extractor::getUser).collect(Collectors.toList()));
        response.setUserDBS(userDBS);
        response.setFrom(login);
        response.setDialogId(body.getDialogId());
        response.setMessages(extractor.getMessagesByIds(messageIds));
        return response;*/
    }
}
