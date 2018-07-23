package common.objects;

import com.alibaba.fastjson.JSONObject;

public class User extends Body {
    private String login;
    private String name;
    private boolean isOnline;
    private long lastOnline;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject();
        o.put("login", login);
        o.put("name", name);
        o.put("isOnline", isOnline);
        o.put("lastOnline", lastOnline);
        return o;
    }
}
