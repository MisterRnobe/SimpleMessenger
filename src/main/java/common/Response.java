package common;

import com.alibaba.fastjson.JSONObject;
import common.objects.Body;

public class Response {
    public static final int OK = 0;
    public static final int ERROR = 1;


    private int status;
    private Integer code = 0; //In case error status
    private JSONObject body;
    private String type;

    public Response() {

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
