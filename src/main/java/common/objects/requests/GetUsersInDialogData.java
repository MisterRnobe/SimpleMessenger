package common.objects.requests;

import common.objects.Body;

public class GetUsersInDialogData extends Body {
    private int dialogId;

    public int getDialogId() {
        return dialogId;
    }

    public GetUsersInDialogData setDialogId(int dialogId) {
        this.dialogId = dialogId;
        return this;
    }
}
