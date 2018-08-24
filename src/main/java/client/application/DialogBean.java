package client.application;

import common.objects.Message;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class DialogBean {
    private final ReadOnlyStringWrapper title;
    private final ReadOnlyObjectWrapper<Message> lastMessage;
    private final ObservableList<String> users;
    private final ObservableList<Message> messages;
    public final int type;
    public final int dialogId;
    public final String creator;

    DialogBean(String title, Message lastMessage, String creator, int type, int dialogId)
    {
        this.title = new ReadOnlyStringWrapper(this, "title", title);
        this.lastMessage = new ReadOnlyObjectWrapper<>(this, "lastMessage", lastMessage);
        this.users = FXCollections.observableArrayList();
        this.messages = FXCollections.observableArrayList();
        this.dialogId = dialogId;
        this.type = type;
        this.creator = creator;
    }
    void setUsers(List<String> users)
    {
        this.users.addAll(users);
    }
    void setMessages(List<Message> messages)
    {
        this.messages.addAll(messages);
    }
    void setLastMessage(Message message)
    {
        this.lastMessage.set(message);
        if (messages.size() != 0)
        {
            messages.add(message);
        }
    }
    public ReadOnlyStringProperty titleProperty()
    {
        return title.getReadOnlyProperty();
    }
    public ReadOnlyObjectProperty<Message> lastMessageProperty()
    {
        return lastMessage.getReadOnlyProperty();
    }
    public ObservableList<String> users()
    {
        return users;
    }
    public ObservableList<Message> messages()
    {
        return messages;
    }
    public List<String> getPartners()
    {
        return users.stream().filter(s->!s.equalsIgnoreCase(ApplicationBank.getInstance().getLogin()))
                .collect(Collectors.toList());
    }
}
