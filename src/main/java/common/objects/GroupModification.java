package common.objects;

import java.util.List;

public class GroupModification extends Body {
    public static final int NEW_USERS = 0;
    public static final int NEW_TITLE = 1;
    public static final int REMOVED_USERS = 2;
    private int type;
    private String title;
    private List<User> users;
    private String from;
    private int dialogId;
    private List<Message> messages;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    public void addMessage(Message m)
    {
        messages.add(m);
    }

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }
}
