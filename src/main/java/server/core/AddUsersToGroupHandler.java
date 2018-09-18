package server.core;

import common.Methods;
import common.Response;
import common.objects.*;
import common.objects.requests.AddUsersToGroupRequest;
import server.DatabaseConnector;
import server.OnlineManager;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AddUsersToGroupHandler extends AbstractHandler<AddUsersToGroupRequest> {
    private List<String> users;
    private GroupModification response;
    public AddUsersToGroupHandler(String login) {
        super(AddUsersToGroupRequest.class, new String[]{"users","dialogId"}, login);
    }

    @Override
    protected Body onHandle(AddUsersToGroupRequest body) throws HandleError {
        DatabaseConnector connector = DatabaseConnector.getInstance();
        users = connector.getUsersInDialog(body.getDialogId()).stream()
                .map(User::getLogin).collect(Collectors.toList());
        connector.addUsersToDialog(body.getDialogId(), body.getUsers());
        LinkedList<Integer> messageIds = new LinkedList<>();
        for(String u: body.getUsers())
        {
            messageIds.add(connector.addMessage(body.getDialogId(), null, user.getName()+" added "+connector.getUser(u).getName(), System.currentTimeMillis()));
        }
        if (messageIds.size() != 0)
        {
             connector.setLastMessage(body.getDialogId(), messageIds.getLast());
        }
        response = new GroupModification();
        response.setType(GroupModification.NEW_USERS);
        response.setUsers(body.getUsers().stream().map(connector::getUser).collect(Collectors.toList()));
        response.setFrom(login);
        response.setDialogId(body.getDialogId());
        response.setMessages(connector.getMessagesByIds(messageIds));
        return response;
    }

    @Override
    protected void beforeSend(Response r) {
        users.remove(login);
        OnlineManager.getInstance().sendAll(r, users);
        DialogInfo d = DatabaseConnector.getInstance().getFullDialog(response.getDialogId()).getDialogInfo();
        Response response = new Response();
        response.setType(Methods.JOIN_GROUP);
        response.setStatus(0);
        response.setBody(d.toJSONObject());
        List<String> newUsers = d.getUsers().stream().map(User::getLogin).collect(Collectors.toList());
        newUsers.remove(login);
        newUsers.removeAll(users);
        OnlineManager.getInstance().sendAll(response, newUsers);
    }
}
