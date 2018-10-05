package common.objects.requests;

import com.alibaba.fastjson.JSONObject;
import common.objects.Body;

public class DialogListRequest extends Body {
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject();
        o.put("count", count);
        return o;
    }
}
