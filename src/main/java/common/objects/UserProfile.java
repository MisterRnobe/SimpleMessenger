package common.objects;

public class UserProfile extends Body {
    private String email, info;
    private String login;
    private String name;
    private String avatarPath;

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

    public String getLogin() {
        return login;
    }


    public UserProfile setLogin(String login) {
        this.login = login;
        return this;
    }


    public String getName() {
        return name;
    }

    public UserProfile setName(String name) {
        this.name = name;
        return this;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public UserProfile setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
        return this;
    }
}
