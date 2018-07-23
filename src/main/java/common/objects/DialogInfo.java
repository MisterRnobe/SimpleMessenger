package common.objects;

import java.util.List;
import java.util.stream.Collectors;

public class DialogInfo {
    private int dialogId;
    private String dialogName;
    private String creator;
    private Message lastMessage;
    private List<String> users;

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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
    public void addUser(String user)
    {
        users.add(user);
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
    public List<String> getPartners(String login)
    {
        return users.stream().filter(s->!s.equals(login)).collect(Collectors.toList());
    }
}
