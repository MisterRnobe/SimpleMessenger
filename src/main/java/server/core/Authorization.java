package server.core;

import common.Request;
import server.DatabaseConnector;

public class Authorization {
    public static Response auth(Request request)
    {
        String token = request.getBody().get("token");
        Response response = new Response();
        response.setType(Response.AUTHORIZATION);
        if (!DatabaseConnector.getInstance().checkToken(request.getBody().get("login"), token))
        {
            response.setStatus(Response.ERROR);
        }
        else
        {
            response.setStatus(Response.OK);
        }
        return response;
    }
}
