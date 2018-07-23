package client.application;

import client.window.main.dialog.DialogController;
import common.objects.*;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ApplicationBank {
    private static final ApplicationBank instance = new ApplicationBank();
    private Stage stage;
    private final List<DialogInfo> dialogs = new LinkedList<>();
    private final List<Listener<DialogInfo>> dialogInfoListeners = new LinkedList<>();

    private final Map<Integer, Dialog> dialogMap = new TreeMap<>();
    private final List<Listener<Dialog>> dialogListeners = new LinkedList<>();

    private final List<Listener<Message>> messageListeners = new LinkedList<>();
    private final Map<String, User> userMap = new TreeMap<>();

    private final List<Listener<User>> userStatusListeners = new LinkedList<>();

    private String login;


    private ApplicationBank(){}
    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void addDialogInfoListener(Listener<DialogInfo> listener)
    {
        dialogInfoListeners.add(listener);
    }
    public void addDialogsInfo(DialogList list)
    {
        list.getDialogs().forEach(this::addDialogInfo);
    }
    public void addDialogListener(Listener<Dialog> listener)
    {
        dialogListeners.add(listener);
    }
    public void addDialog(Dialog d)
    {
        dialogMap.put(d.getDialogId(), d);
        d.getUsers().forEach(this::addUser);
        dialogListeners.forEach(l->l.onHandle(d));
    }
    public void addDialogInfo(DialogInfo dialogInfo)
    {
        dialogs.add(dialogInfo);
        dialogInfoListeners.forEach(listener -> listener.onHandle(dialogInfo));
    }
    public void addMessage(Message message)
    {
        DialogInfo dialogInfo = dialogs.stream().filter(d->d.getDialogId()==message.getDialogId()).findFirst().get();
        dialogInfo.setLastMessage(message);

        Dialog dialog = dialogMap.get(message.getDialogId());
        if (dialog != null)
        {
            dialog.addMessage(message);
        }
        messageListeners.forEach(l->l.onHandle(message));
    }
    public void addMessageListener(Listener<Message> messageListener)
    {
        messageListeners.add(messageListener);
    }
    public void addUser(User u)
    {
        userMap.put(u.getLogin(), u);
    }
    public User getUserByLogin(String login)
    {
        return userMap.get(login);
    }
    public void updateUserStatus(User u)
    {
        User user = userMap.get(u.getLogin());
        if (user != null)
        {
            user.setOnline(u.isOnline());
            user.setLastOnline(u.getLastOnline());
        }
        else
        {
            userMap.put(u.getLogin(), u);
        }
        userStatusListeners.forEach(l->l.onHandle(u));
    }
    public void addUserStatusListener(Listener<User> u){userStatusListeners.add(u);}

    public static ApplicationBank getInstance() {
        return instance;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public DialogInfo getDialogInfoById(int id)
    {
        return dialogs.stream().filter(d->d.getDialogId() == id).findAny().get();
    }
    public Dialog getDialogById(int id)
    {
        return dialogMap.get(id);
    }
}
