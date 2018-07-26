package client.application;

import common.objects.*;
import javafx.collections.*;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class ApplicationBank {
    private static final ApplicationBank instance = new ApplicationBank();
    private Stage stage;
    private final ObservableMap<Integer, DialogBean> dialogs = FXCollections.observableHashMap();

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
    public void addDialogsInfo(DialogList list)
    {
        list.getDialogs().forEach(this::addDialogInfo);
    }
    public void addDialog(Dialog d)
    {
        DialogBean dialogBean = dialogs.get(d.getDialogId());
        dialogBean.setMessages(d.getMessages());
        dialogBean.setUsers(d.getUsers().stream().map(User::getLogin).collect(Collectors.toList()));
        d.getUsers().forEach(u->userMap.put(u.getLogin(), u));
    }
    public void addDialogInfo(DialogInfo dialogInfo)
    {
        DialogBean dialogBean = new DialogBean(dialogInfo.getDialogName(),
                dialogInfo.getLastMessage(), dialogInfo.getDialogId());
        dialogs.put(dialogBean.dialogId, dialogBean);
    }
    public void addMessage(Message message)
    {
        DialogBean bean = dialogs.get(message.getDialogId());
        bean.setLastMessage(message);
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
    public DialogBean getDialogById(int id)
    {
        return dialogs.get(id);
    }
    public void addDialogListener(MapChangeListener<? super Integer, ? super DialogBean> listener)
    {
        dialogs.addListener(listener);
    }
}
