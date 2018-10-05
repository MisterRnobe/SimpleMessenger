package server.core;

import common.objects.Body;
import common.objects.requests.RegistrationData;
import server.database.DatabaseConnectorOld;
import server.database.DatabaseManager;
import server.utils.FIleSaver;

import java.sql.SQLException;

import static server.utils.DataVerifier.*;

public class RegistrationHandler extends AbstractHandler<RegistrationData> {
    public RegistrationHandler() {
        super(RegistrationData.class, new String[]{"login", "password", "name","email"}, null);
    }




    @Override
    protected Body onHandle(RegistrationData body) throws HandleError, SQLException {

        checkLogin(body.getLogin());
        checkPassword(body.getPassword());
        checkName(body.getName());
        checkMail(body.getEmail());
        checkInfo(body.getInfo());
        DatabaseManager.getExtractor().addUser(body);
        if (body.getAvatar() != null)
            FIleSaver.saveAvatar(body.getAvatar(), body.getLogin()+".png");
        return Body.NULL_BODY;
    }

}
