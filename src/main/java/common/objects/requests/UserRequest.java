package common.objects.requests;

import common.objects.Body;

public class UserRequest extends Body {
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
