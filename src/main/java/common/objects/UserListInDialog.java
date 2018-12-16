package common.objects;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class UserListInDialog extends Body{
    private int dialogId;

    public int getDialogId() {
        return dialogId;
    }

    public UserListInDialog setDialogId(int dialogId) {
        this.dialogId = dialogId;
        return this;
    }

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    public void addUser(User u)
    {
        this.users.add(u);
    }

    @Override
    public JSONObject toJSONObject() {
        return new JSONObject()
                .fluentPut("users", users)
                .fluentPut("dialogId", dialogId);
    }
}
