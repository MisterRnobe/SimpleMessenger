package server.core;

import common.objects.Body;
import common.objects.User;
import server.DatabaseConnector;
import server.OnlineManager;

public class GetUserStatusHandler extends AbstractHandler<User> {
    public GetUserStatusHandler() {
        super(User.class, new String[]{"login"}, null);
    }

    @Override
    protected Body onHandle(User body) throws HandleError {
        User u = DatabaseConnector.getInstance().getUserStatus(body.getLogin());
        u.setOnline(OnlineManager.getInstance().isOnline(body.getLogin()));
        u.setLogin(body.getLogin());
        return u;
    }
}
