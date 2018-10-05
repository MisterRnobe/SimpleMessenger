package server.core;

import common.objects.Body;
import common.objects.requests.UserRequest;
import server.database.DatabaseConnectorOld;
import server.database.DatabaseManager;

import java.sql.SQLException;

public class UserProfileHandler extends AbstractHandler<UserRequest> {
    public UserProfileHandler() {
        super(UserRequest.class, new String[]{"login"}, null);
    }

    @Override
    protected Body onHandle(UserRequest body) throws HandleError, SQLException {
        return DatabaseManager.getExtractor().getUserProfile(body.getLogin());
    }
}
