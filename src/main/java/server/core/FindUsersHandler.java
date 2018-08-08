package server.core;

import common.objects.Body;
import common.objects.User;
import common.objects.UserList;
import common.objects.requests.FindUsersRequest;
import server.DatabaseConnector;

import java.util.List;

public class FindUsersHandler extends AbstractHandler<FindUsersRequest>{
    public FindUsersHandler() {
        super(FindUsersRequest.class, new String[]{"mask"}, null);
    }

    @Override
    protected Body onHandle(FindUsersRequest body) throws HandleError {
        List<User> users = DatabaseConnector.getInstance().findUsers(body.getMask());
        UserList userList = new UserList();
        userList.setUsers(users);
        return userList;
    }
}
