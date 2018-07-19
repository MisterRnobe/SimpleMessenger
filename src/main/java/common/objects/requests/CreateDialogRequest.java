package common.objects.requests;

import com.alibaba.fastjson.JSONObject;
import common.objects.Body;

public class CreateDialogRequest extends Body {
    private String partner;
    private String initialMessage;

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getInitialMessage() {
        return initialMessage;
    }

    public void setInitialMessage(String initialMessage) {
        this.initialMessage = initialMessage;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject();
        o.put("partner", partner);
        o.put("initialMessage", initialMessage);
        return o;
    }
}
