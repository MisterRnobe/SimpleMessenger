package common.objects;

public class DialogInfo {
    private int dialogId;
    private String dialogName;
    private String creator;
    private Message lastMessage;

    public DialogInfo(int dialogId, String dialogName, String creator) {
        this.dialogId = dialogId;
        this.dialogName = dialogName;
        this.creator = creator;
    }
    public DialogInfo(){}

    public int getDialogId() {
        return dialogId;
    }

    public String getDialogName() {
        return dialogName;
    }

    public String getCreator() {
        return creator;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    public void setDialogName(String dialogName) {
        this.dialogName = dialogName;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
