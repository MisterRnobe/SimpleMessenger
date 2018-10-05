package server.core;

import common.Errors;
import common.Request;
import common.Response;
import common.objects.Body;

import java.sql.SQLException;

public abstract class AbstractHandler<T extends Body> {
    private final Class<T> requiredClass;
    private final String[] requiredFields;
    protected final String login;
    public AbstractHandler(Class<T> requiredClass, String[] requiredFields, String login)
    {
        this.requiredClass = requiredClass;
        this.requiredFields = requiredFields;
        this.login = login;
    }
    protected abstract Body onHandle(T body) throws HandleError, SQLException;
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
            catch (SQLException sql)
            {
                sql.printStackTrace();
                r.setStatus(Response.ERROR);
                r.setCode(Errors.INTERNAL_ERROR);
            }
        }
        return r;
    }
    protected void beforeSend(Response response)
    {

    }
}
