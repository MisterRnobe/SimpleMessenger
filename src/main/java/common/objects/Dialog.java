package common.objects;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class Dialog extends Body {
    private int dialogId;
    private List<User> users;
    private List<Message> messages;

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    public void addUser(User u)
    {
        users.add(u);
    }
    public void addMessage(Message m)
    {
        messages.add(m);
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject();
        o.put("users", users);
        o.put("dialogId", dialogId);
        o.put("messages", messages);
        return o;
    }
}
