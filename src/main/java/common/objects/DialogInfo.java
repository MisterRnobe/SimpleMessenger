package common.objects;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class DialogInfo extends DialogMarker{

    private User partner;

    public DialogInfo(){}



    public User getPartner() {
        return partner;
    }

    public DialogInfo setPartner(User partner) {
        this.partner = partner;
        return this;
    }

    @Override
    public JSONObject toJSONObject() {
        return super.toJSONObject().fluentPut("partner", getPartner());
    }
}
