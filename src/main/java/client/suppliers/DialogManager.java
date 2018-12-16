package client.suppliers;

import com.alibaba.fastjson.JSONObject;
import common.objects.Message;
import common.objects.User;
import common.objects.requests.DialogTypes;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.List;
import java.util.stream.Collectors;

//This class stores and supplies dialogs
public class DialogManager {
    private static DialogManager instance;

    private final ObservableMap<Integer, AbstractDialogBean> allDialogs = FXCollections.observableHashMap();


    private DialogManager() {
    }

    public void addDialog(JSONObject dialog) {
        AbstractDialogBean abstractDialogBean = dialog.getInteger("type") == DialogTypes.DIALOG ?
                buildDialog(dialog) : buildGroup(dialog);
        allDialogs.put(abstractDialogBean.dialogId, abstractDialogBean);
    }

    public void addDialogListener(MapChangeListener<Integer, AbstractDialogBean> listener) {
        allDialogs.addListener(listener);
    }


    private DialogBean buildDialog(JSONObject object) {
        return new DialogBean(object.getInteger("dialogId"),
                JSONObject.parseObject(object.getJSONObject("lastMessage").toJSONString(), Message.class),
                JSONObject.parseObject(object.getJSONObject("partner").toJSONString(), User.class),
                object.getInteger("unread"));
    }

    private GroupBean buildGroup(JSONObject object) {
        return new GroupBean(object.getInteger("dialogId"),
                object.getInteger("type"),
                object.getString("groupName"),
                object.getString("creator"),
                JSONObject.parseObject(object.getJSONObject("lastMessage").toJSONString(), Message.class),
                object.getInteger("unread"),
                object.getString("avatarPath"),
                object.getInteger("usersCount"));
    }

    public AbstractDialogBean getDialogById(int id) {
        return allDialogs.get(id);
    }

    public void addMessages(int dialogId, List<Message> messages) {
        AbstractDialogBean abstractDialogBean = allDialogs.get(dialogId);
        if (abstractDialogBean != null) {
            abstractDialogBean.setMessages(messages);
        }
    }

    public void addMessage(Message m) {
        AbstractDialogBean abstractDialogBean = allDialogs.get(m.getDialogId());
        if (abstractDialogBean != null) {
            abstractDialogBean.setLastMessage(m);
        }
    }

    public List<DialogBean> getDialogsOnly() {
        return allDialogs.entrySet().stream().filter(e -> e.getValue().type == DialogTypes.DIALOG)
                .map(e -> (DialogBean) e.getValue()).collect(Collectors.toList());
    }

    public void readMessages(int dialogId) {
        allDialogs.get(dialogId).readMessages();
    }

    public static DialogManager getInstance() {
        if (instance == null)
            instance = new DialogManager();
        return instance;
    }
}
