package server.core;

import common.Errors;
import org.apache.commons.codec.digest.DigestUtils;
import server.DatabaseConnector;

import java.util.Map;

public class Login {
    public static Response login(Map<String, String> map)
    {
        Response response = new Response();
        response.setType(Response.LOGIN);
        if (!DatabaseConnector.getInstance().checkUserExistence("login", map.get("login"))) {
            response.setStatus(Response.ERROR);
            response.setCode(Errors.NOT_EXIST);
        }
        else if (!DatabaseConnector.getInstance().verifyUser(map.get("login"), map.get("password")))
        {
            response.setStatus(Response.ERROR);
            response.setCode(Errors.BAD_PASSWORD);
        }
        else
        {
            String token = generateToken(map.get("login"));
            response.setStatus(Response.OK);
            response.setBody(token);
            DatabaseConnector.getInstance().setToken(map.get("login"), token);
        }
        return response;
    }
    private static String generateToken(String login)
    {
        return DigestUtils.sha256Hex(login + System.currentTimeMillis());
    }
}
