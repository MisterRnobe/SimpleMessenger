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
    private final Map<String, List<Listener<User>>> userStatusListeners = new HashMap<>();

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

    }
    public void addDialogInfo(DialogInfo dialogInfo)
    {
        String title = dialogInfo.getDialogName();
        if (title == null)
            title = dialogInfo.getUsers().stream()
                    .filter(user -> !user.getLogin().equalsIgnoreCase(login))
                    .map(User::getName)
                    .collect(Collectors.joining(", "));

        DialogBean dialogBean = new DialogBean(title,
                dialogInfo.getLastMessage(), dialogInfo.getType(),dialogInfo.getDialogId());
        dialogBean.setUsers(dialogInfo.getUsers().stream().map(User::getLogin).collect(Collectors.toList()));
        dialogInfo.getUsers().forEach(u->userMap.put(u.getLogin(), u));
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

        List<Listener<User>> list = userStatusListeners.get(u.getLogin());
        if (list != null)
            list.forEach(listener -> listener.onHandle(u));
    }
    public void addUserStatusListener(Listener<User> u, String login){
        List<Listener<User>> list = userStatusListeners.get(login);
        if (list!=null)
            list.add(u);
        else
            userStatusListeners.put(login, Arrays.asList(u));
    }

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
    public List<DialogBean> getDialogs()
    {
        return dialogs.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
    public List<User> getFriendList()
    {
        return dialogs.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(d->d.type==DialogInfo.DIALOG)
                .map(d1->userMap.get(d1.getPartners().get(0)))
                .collect(Collectors.toList());
    }
}
