package server.core;

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

public class AddUsersToGroupHandler extends AbstractHandler<AddUsersToGroupRequest> {
    private List<String> users;
    private GroupModification response;
    public AddUsersToGroupHandler(String login) {
        super(AddUsersToGroupRequest.class, new String[]{"users","dialogId"}, login);
    }

    @Override
    protected Body onHandle(AddUsersToGroupRequest body) throws HandleError, SQLException {
        //DatabaseConnectorOld connector = DatabaseConnectorOld.getInstance();
        DatabaseExtractor extractor = DatabaseManager.getExtractor();
        users = extractor.getUsersInDialog(body.getDialogId()).stream()
                .map(User::getLogin).collect(Collectors.toList());
        extractor.addUsersToDialog(body.getDialogId(), body.getUsers());
        LinkedList<Integer> messageIds = new LinkedList<>();
        for(String u: body.getUsers())
        {
            messageIds.add(extractor.addMessage(body.getDialogId(), null, login+" added "+u, System.currentTimeMillis()));
        }
        if (messageIds.size() != 0)
        {
             extractor.setLastMessage(body.getDialogId(), messageIds.getLast());
        }
        response = new GroupModification();
        response.setType(GroupModification.NEW_USERS);
        List<User> users = new LinkedList<>();
        for (String login: body.getUsers())
        {
            users.add(extractor.getUser(login));
        }
        //response.setUsers(body.getUsers().stream().map(extractor::getUser).collect(Collectors.toList()));
        response.setUsers(users);
        response.setFrom(login);
        response.setDialogId(body.getDialogId());
        response.setMessages(extractor.getMessagesByIds(messageIds));
        return response;
    }

    @Override
    protected void beforeSend(Response r) {
        DatabaseExtractor extractor = DatabaseManager.getExtractor();
        users.remove(login);
        OnlineManager.getInstance().sendAll(r, users);
        //DatabaseConnectorOld.getInstance().getFullDialog(response.getDialogId()).getDialogInfo();
        try {
            DialogInfo d = extractor.getFullDialog(response.getDialogId()).getDialogInfo();
            Response response = new Response();
            response.setType(Methods.JOIN_GROUP);
            response.setStatus(0);
            response.setBody(d.toJSONObject());
            List<String> newUsers = d.getUsers().stream().map(User::getLogin).collect(Collectors.toList());
            newUsers.remove(login);
            newUsers.removeAll(users);
            OnlineManager.getInstance().sendAll(response, newUsers);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
