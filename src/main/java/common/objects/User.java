package common.objects;

public class User extends Body {
    private String login;
    private String name;
    private boolean isOnline;
    private long lastOnline;
    private String avatarPath;

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

    public String getAvatarPath() {
        return avatarPath;
    }

    public User setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
        return this;
    }
}
