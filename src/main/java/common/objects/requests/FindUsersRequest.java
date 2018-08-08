package common.objects.requests;

import com.alibaba.fastjson.JSONObject;
import common.objects.Body;

public class FindUsersRequest extends Body {
    private String mask;

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject();
        o.put("mask", mask);
        return o;
    }
}
