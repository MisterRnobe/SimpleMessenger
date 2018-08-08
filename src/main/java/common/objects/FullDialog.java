package common.objects;

import com.alibaba.fastjson.JSONObject;

public class FullDialog extends Body {
    private DialogInfo dialogInfo;
    private Dialog dialog;

    public DialogInfo getDialogInfo() {
        return dialogInfo;
    }

    public void setDialogInfo(DialogInfo dialogInfo) {
        this.dialogInfo = dialogInfo;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject();
        o.put("dialogInfo", dialogInfo);
        o.put("dialog", dialog);
        return o;
    }
}
