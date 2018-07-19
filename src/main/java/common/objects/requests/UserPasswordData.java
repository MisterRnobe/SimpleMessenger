package common.objects.requests;

import com.alibaba.fastjson.JSONObject;
import common.objects.Body;

public class UserPasswordData extends Body {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject();
        o.put("login", login);
        o.put("password", password);
        return o;
    }
}
