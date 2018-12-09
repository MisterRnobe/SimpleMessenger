package common.objects;

public class DialogInfo extends DialogMarker{

    private int dialogId;
    private int type;
    private Message lastMessage;
    private User partner;
    private int unread;

    public DialogInfo(){}

    public int getDialogId() {
        return dialogId;
    }

    public DialogInfo setDialogId(int dialogId) {
        this.dialogId = dialogId;
        return this;
    }

    public int getType() {
        return type;
    }

    public DialogInfo setType(int type) {
        this.type = type;
        return this;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public DialogInfo setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    public User getPartner() {
        return partner;
    }

    public DialogInfo setPartner(User partner) {
        this.partner = partner;
        return this;
    }

    public int getUnread() {
        return unread;
    }

    public DialogInfo setUnread(int unread) {
        this.unread = unread;
        return this;
    }
}
