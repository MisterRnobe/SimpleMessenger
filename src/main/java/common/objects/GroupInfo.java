package common.objects;

public class GroupInfo extends DialogMarker{

    private int dialogId;
    private int type;
    private String groupName;
    private String creator;
    private Message lastMessage;
    private int unread;
    private String avatarPath;
    private int usersCount;

    public int getDialogId() {
        return dialogId;
    }

    public GroupInfo setDialogId(int dialogId) {
        this.dialogId = dialogId;
        return this;
    }

    public int getType() {
        return type;
    }

    public GroupInfo setType(int type) {
        this.type = type;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public GroupInfo setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public GroupInfo setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public GroupInfo setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    public int getUnread() {
        return unread;
    }

    public GroupInfo setUnread(int unread) {
        this.unread = unread;
        return this;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public GroupInfo setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
        return this;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public GroupInfo setUsersCount(int usersCount) {
        this.usersCount = usersCount;
        return this;
    }
}
