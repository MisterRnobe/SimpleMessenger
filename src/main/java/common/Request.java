package common;

import com.alibaba.fastjson.JSONObject;

public class Request {
    private String method;
    private JSONObject body;

    public String getMethod() {
        return method;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public JSONObject getBody() {
        return body;
    }

    public Request setBody(JSONObject body) {
        this.body = body;
        return this;
    }
}
