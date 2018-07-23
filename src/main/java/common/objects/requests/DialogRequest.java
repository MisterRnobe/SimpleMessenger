package common.objects.requests;

import com.alibaba.fastjson.JSONObject;
import common.objects.Body;

public class DialogRequest extends Body {
    private int dialogId;

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject();
        o.put("dialogId", dialogId);
        return o;
    }
}
