package common.objects.requests;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.objects.Body;

public class DialogListRequest extends Body {
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject();
        o.put("count", count);
        return o;
    }
}
