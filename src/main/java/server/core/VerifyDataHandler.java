package server.core;

import common.objects.Body;
import common.objects.requests.RegistrationData;
import static server.utils.DataVerifier.*;

public class VerifyDataHandler extends AbstractHandler<RegistrationData> {
    public VerifyDataHandler() {
        super(RegistrationData.class, new String[]{"login", "password", "name","email"}, null);
    }

    @Override
    protected Body onHandle(RegistrationData body) throws HandleError {
        checkLogin(body.getLogin());
        checkPassword(body.getPassword());
        checkName(body.getName());
        checkMail(body.getEmail());
        checkInfo(body.getInfo());
        return Body.NULL_BODY;
    }
}
