package common.objects.requests;

import common.objects.Body;

import java.util.List;

public class AddUsersToGroupRequest extends Body {
    private int dialogId;
    private List<String> users;

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
    public void addUser(String user)
    {
        this.users.add(user);
    }
}
