package common.objects;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class UserList extends Body{
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
        JSONObject o = new JSONObject();
        o.put("userDBS", users);
        return o;
    }
}
