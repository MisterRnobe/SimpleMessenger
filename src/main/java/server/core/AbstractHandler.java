package server.core;

import common.Errors;
import common.Methods;
import common.Request;
import common.Response;
import common.objects.Body;
import common.objects.Message;
import common.objects.User;
import server.OnlineManager;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractHandler<T extends Body> {
    private final Class<T> requiredClass;
    private final String[] requiredFields;
    protected final String login;

    public AbstractHandler(Class<T> requiredClass, String[] requiredFields, String login) {
        this.requiredClass = requiredClass;
        this.requiredFields = requiredFields;
        this.login = login;
    }

    protected abstract Body onHandle(T body) throws HandleError, SQLException;

    public Response handle(Request request) {
        Response r = new Response();
        r.setType(request.getMethod());
        if (request.getBody() == null) {
            r.setStatus(Response.ERROR);
            r.setCode(Errors.EMPTY_BODY);
        }
        boolean missing = false;
        for (String s : requiredFields) {
            if (request.getBody().get(s) == null) {
                missing = true;
                break;
            }
        }
        if (missing) {
            r.setStatus(Response.ERROR);
            r.setCode(Errors.WRONG_REQUEST_PARAMETERS);
        } else {
            try {

                Body b = onHandle(request.getBody().toJavaObject(requiredClass));
                r.setStatus(Response.OK);
                r.setBody(b.toJSONObject());
                beforeSend();
            } catch (HandleError e) {
                r.setStatus(Response.ERROR);
                r.setCode(e.getCode());
            } catch (SQLException sql) {
                sql.printStackTrace();
                r.setStatus(Response.ERROR);
                r.setCode(Errors.INTERNAL_ERROR);
            }
        }
        return r;
    }

    protected void beforeSend() {

    }

    protected static void sendAllMessage(Message message, List<User> addressees) {
        Response messageToUsers = new Response();
        messageToUsers.setStatus(Response.OK);
        messageToUsers.setType(Methods.NEW_MESSAGE);
        messageToUsers.setBody(message.toJSONObject());
        OnlineManager.getInstance().sendAll(messageToUsers, addressees.stream()
                .map(User::getLogin)
                .collect(Collectors.toList()));
    }
}
