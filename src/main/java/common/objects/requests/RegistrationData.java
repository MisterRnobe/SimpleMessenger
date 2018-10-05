package common.objects.requests;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.objects.Body;

public class RegistrationData extends Body {
    private String login;
    private String password;
    private String name;
    private String email;
    private String info;
    private byte[] avatar;

    public RegistrationData() {
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
