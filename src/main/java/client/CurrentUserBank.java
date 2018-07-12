package client;

public class CurrentUserBank {
    private static CurrentUserBank ourInstance = new CurrentUserBank();
    public static CurrentUserBank getInstance() {
        return ourInstance;
    }
    private CurrentUserBank() {
    }

    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
