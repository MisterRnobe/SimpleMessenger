package common.objects;

public abstract class DialogMarker extends Body {
    private int dialogId;
    private int type;
    private Message lastMessage;
    private int unread;

    public int getDialogId() {
        return dialogId;
    }

    public DialogMarker setDialogId(int dialogId) {
        this.dialogId = dialogId;
        return this;
    }

    public int getType() {
        return type;
    }

    public DialogMarker setType(int type) {
        this.type = type;
        return this;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public DialogMarker setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    public int getUnread() {
        return unread;
    }

    public DialogMarker setUnread(int unread) {
        this.unread = unread;
        return this;
    }
}
