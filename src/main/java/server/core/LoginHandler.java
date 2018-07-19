package server.core;

import common.Errors;
import common.objects.Body;
import common.objects.requests.UserPasswordData;
import server.DatabaseConnector;

public class LoginHandler extends AbstractHandler<UserPasswordData> {
    public LoginHandler() {
        super(UserPasswordData.class, new String[]{"login", "password"}, null);
    }

    @Override
    protected Body onHandle(UserPasswordData body) throws HandleError {

        if (!DatabaseConnector.getInstance().checkUserExistence("login", body.getLogin())) {
            throw new HandleError(Errors.NOT_EXIST);
        }
        else if (!DatabaseConnector.getInstance().verifyUser(body.getLogin(), body.getPassword()))
        {
            throw new HandleError(Errors.BAD_PASSWORD);
        }
        else
        {
            return Body.NULL_BODY;
        }
    }
}
