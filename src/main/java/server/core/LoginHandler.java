package server.core;

import common.Errors;
import common.objects.Body;
import common.objects.requests.UserPasswordData;
import server.database.DatabaseExtractor;
import server.database.DatabaseManager;

import java.sql.SQLException;

public class LoginHandler extends AbstractHandler<UserPasswordData> {
    public LoginHandler() {
        super(UserPasswordData.class, new String[]{"login", "password"}, null);
    }

    @Override
    protected Body onHandle(UserPasswordData body) throws HandleError, SQLException {
        DatabaseExtractor extractor = DatabaseManager.getExtractor();
        if (!extractor.verifyUser(body))
        {
            throw new HandleError(Errors.BAD_PASSWORD);
        }
        else
        {
            return Body.NULL_BODY;
        }
    }
}
