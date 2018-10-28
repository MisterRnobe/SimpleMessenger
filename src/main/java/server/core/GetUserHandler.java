package server.core;

import common.objects.Body;
import common.objects.requests.UserRequest;
import server.database.DatabaseManager;

import java.sql.SQLException;

public class GetUserHandler extends AbstractHandler<UserRequest>{
    public GetUserHandler() {
        super(UserRequest.class, new String[]{"login"}, null);
    }

    @Override
    protected Body onHandle(UserRequest body) throws HandleError, SQLException {
        return DatabaseManager.getExtractor().getUser(body.getLogin());
    }
}
