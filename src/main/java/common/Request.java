package common;

import java.util.Map;

public class Request {
    private String method;
    private Map<String, String> body;

    public String getMethod() {
        return method;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public Request setBody(Map<String, String> body) {
        this.body = body;
        return this;
    }
}
