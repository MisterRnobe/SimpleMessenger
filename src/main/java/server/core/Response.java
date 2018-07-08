package server.core;

import java.util.Map;

public class Response {
    public static final int OK = 0;
    public static final int ERROR = 1;

    public static final String REGISTRATION = "registration";
    public static final String LOGIN = "login";
    public static final String AUTHORIZATION = "auth";
    public static final String DIALOG_CREATION = "dialog_creation";
    public static final String SEND_MESSAGE = "send_message";


    private int status;
    private int code; //In case error status
    private Map<String, String> body;
    private String type;
    public Response()
    {

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
