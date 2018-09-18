package client.network.queries;

import client.network.ClientSocket;
import client.utils.ApplicationBank;
import client.utils.DialogBean;
import com.alibaba.fastjson.JSON;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.DialogInfo;
import common.objects.GroupModification;
import common.objects.User;
import common.objects.requests.AddUsersToGroupRequest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GroupModificationQuery {
    public static void addUsers(int dialogId, List<String> users) throws IOException {
        AddUsersToGroupRequest request = new AddUsersToGroupRequest();
        request.setDialogId(dialogId);
        request.setUsers(users);
        ClientSocket.getInstance().send(new Request().setMethod(Methods.MODIFY_GROUP).setBody(request.toJSONObject()));
    }
    public static void onHandle(Response response)
    {
        if (response.getStatus() == Response.OK)
        {
            GroupModification modification = JSON.parseObject(response.getBody().toJSONString(), GroupModification.class);
            if (modification.getType() == GroupModification.NEW_USERS)
            {
                DialogBean b = ApplicationBank.getInstance().getDialogById(modification.getDialogId());
                b.users().addAll(modification.getUsers().stream().map(User::getLogin).collect(Collectors.toList()));
                modification.getUsers().forEach(user -> ApplicationBank.getInstance().addUser(user));
                modification.getMessages().forEach(m->ApplicationBank.getInstance().addMessage(m));
            }
        }
    }
    public static void onJoinedGroup(Response r)
    {
        DialogInfo dialogInfo = JSON.parseObject(r.getBody().toJSONString(), DialogInfo.class);
        ApplicationBank.getInstance().addDialogInfo(dialogInfo);
    }
}
