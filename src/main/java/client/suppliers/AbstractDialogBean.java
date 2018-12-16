package client.suppliers;

import common.objects.Message;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public abstract class AbstractDialogBean {
    protected final ReadOnlyObjectWrapper<Message> lastMessage;
    protected final ObservableList<Message> messages;
    protected final ReadOnlyIntegerWrapper unreadCount;
    protected final ReadOnlyStringWrapper avatarPath;
    protected final ReadOnlyStringWrapper title;
    public final int type;
    public final int dialogId;

    protected AbstractDialogBean(int type, int dialogId) {
        lastMessage = new ReadOnlyObjectWrapper<>(this, "lastMessage");
        messages = FXCollections.observableArrayList();
        unreadCount = new ReadOnlyIntegerWrapper(this, "unreadCount");
        avatarPath = new ReadOnlyStringWrapper(this, "avatarPath");
        title = new ReadOnlyStringWrapper(this, "title");
        this.type = type;
        this.dialogId = dialogId;
    }

    void setMessages(List<Message> messages) {
        this.messages.addAll(messages);
        this.lastMessage.set(messages.get(messages.size() - 1));
    }

    void setLastMessage(Message message) {
        this.lastMessage.set(message);
        if (messages.size() != 0) {
            messages.add(message);
        }
    }

    public void addUnreadCount(int count) {
        unreadCount.set(unreadCount.getValue() + count);
    }

    public void readMessages() {
        this.unreadCount.set(0);
    }

    public ReadOnlyObjectProperty<Message> lastMessageProperty() {
        return lastMessage.getReadOnlyProperty();
    }

    public ObservableList<Message> messages() {
        return messages;
    }

    public ReadOnlyIntegerProperty unreadCountProperty() {
        return unreadCount.getReadOnlyProperty();
    }

    public ReadOnlyStringProperty avatarPath() {
        return avatarPath.getReadOnlyProperty();
    }

    public ReadOnlyStringProperty title() {
        return title.getReadOnlyProperty();
    }
}
