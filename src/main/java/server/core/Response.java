package server.core;

public class Response {
    public static final int OK = 0;
    public static final int ERROR = 1;

    public static final String REGISTRATION = "registration";
    public static final String LOGIN = "login";


    private int status;
    private int code; //In case error status
    private Object body;
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

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
