package common.objects;

import java.util.List;

public class DialogInfo extends Body{
    public static final int DIALOG = 0;
    public static final int GROUP = 1;
    public static final int CHANNEL = 2;
    private int dialogId;
    private int type;
    private String dialogName;
    private String creator;
    private Message lastMessage;
    private List<User> users;
    private int unread;

    public DialogInfo(int dialogId, String dialogName, String creator, int type) {
        this.dialogId = dialogId;
        this.dialogName = dialogName;
        this.creator = creator;
        this.type = type;
    }
    public DialogInfo(){}

    public int getDialogId() {
        return dialogId;
    }

    public String getDialogName() {
        return dialogName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    public void addUser(User user)
    {
        users.add(user);
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
