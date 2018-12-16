package common.objects.requests;

import common.objects.Body;

public class DialogIdRequest extends Body {
    private int dialogId;

    public int getDialogId() {
        return dialogId;
    }

    public DialogIdRequest setDialogId(int dialogId) {
        this.dialogId = dialogId;
        return this;
    }
}
