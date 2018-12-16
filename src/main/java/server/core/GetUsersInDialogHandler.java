package server.core;

import common.objects.Body;
import common.objects.User;
import common.objects.UserList;
import common.objects.UserListInDialog;
import common.objects.requests.GetUsersInDialogData;
import server.database.DatabaseExtractor;
import server.database.DatabaseManager;

import java.sql.SQLException;
import java.util.List;

public class GetUsersInDialogHandler extends AbstractHandler<GetUsersInDialogData>{
    public GetUsersInDialogHandler() {
        super(GetUsersInDialogData.class, new String[]{"dialogId"}, null);
    }

    @Override
    protected Body onHandle(GetUsersInDialogData body) throws HandleError, SQLException {
        DatabaseExtractor extractor = DatabaseManager.getExtractor();
        List<User> usersInDialog = extractor.getUsersInDialog(body.getDialogId());
        UserListInDialog userList = new UserListInDialog();
        userList.setUsers(usersInDialog);
        userList.setDialogId(body.getDialogId());
        return userList;
    }
}
