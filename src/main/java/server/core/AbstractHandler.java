package server.core;

import common.Errors;
import common.Request;
import common.Response;
import common.objects.Body;
import common.objects.User;
import server.DatabaseConnector;

public abstract class AbstractHandler<T extends Body> {
    private final Class<T> requiredClass;
    private final String[] requiredFields;
    protected final String login;
    protected User user;
    public AbstractHandler(Class<T> requiredClass, String[] requiredFields, String login)
    {
        this.requiredClass = requiredClass;
        this.requiredFields = requiredFields;
        this.login = login;
        if (login != null)
        {
            user = DatabaseConnector.getInstance().getUser(login);
        }
    }
    protected abstract Body onHandle(T body) throws HandleError;
    public Response handle(Request request)
    {

        Response r = new Response();
        r.setType(request.getMethod());
        if (request.getBody() == null)
        {
            r.setStatus(Response.ERROR);
            r.setCode(Errors.EMPTY_BODY);
        }
        boolean missing = false;
        for (String s: requiredFields) {
            if (request.getBody().get(s) == null) {
                missing = true;
                break;
            }
        }
        if (missing)
        {
            r.setStatus(Response.ERROR);
            r.setCode(Errors.WRONG_REQUEST_PARAMETERS);
        }
        else {
            try {

                Body b = onHandle(request.getBody().toJavaObject(requiredClass));
                r.setStatus(Response.OK);
                r.setBody(b.toJSONObject());
                beforeSend(r);
            }
            catch (HandleError e)
            {
                r.setStatus(Response.ERROR);
                r.setCode(e.getCode());
            }
        }
        return r;
    }
    protected void beforeSend(Response response)
    {

    }
}
