package server.core;

import common.objects.Body;
import common.objects.UserList;
import common.objects.requests.FindUsersRequest;
import server.database.DatabaseManager;

import java.sql.SQLException;

public class FindUsersHandler extends AbstractHandler<FindUsersRequest>{
    public FindUsersHandler() {
        super(FindUsersRequest.class, new String[]{"mask"}, null);
    }

    @Override
    protected Body onHandle(FindUsersRequest body) throws HandleError, SQLException {
        UserList userList = new UserList();
        userList.setUsers(DatabaseManager.getExtractor().findUsers(body.getMask()));
        return userList;
    }
}
