package client;

public class CurrentUserBank {
    private static CurrentUserBank ourInstance = new CurrentUserBank();
    public static CurrentUserBank getInstance() {
        return ourInstance;
    }
    private CurrentUserBank() {
    }

}
