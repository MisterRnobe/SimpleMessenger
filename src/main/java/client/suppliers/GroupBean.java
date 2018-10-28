package client.suppliers;

import common.objects.Message;
import common.objects.User;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class GroupBean extends AbstractDialogBean {
    private final ReadOnlyIntegerWrapper membersCount;
    private final ObservableList<User> members = FXCollections.observableArrayList();


    public final String creator;
    protected GroupBean(int dialogId, int type, String groupName, String creator, Message lastMessage, int unread, String avatarPath, int usersCount) {
        super(type, dialogId);
        title.set(groupName);
        this.creator = creator;
        this.lastMessage.set(lastMessage);
        this.unreadCount.set(unread);
        this.avatarPath.set(avatarPath);
        this.membersCount = new ReadOnlyIntegerWrapper(this, "membersCount", usersCount);
    }
    public ReadOnlyIntegerProperty membersCount()
    {
        return membersCount.getReadOnlyProperty();
    }
    public List<User> getMembers()
    {
        return new ArrayList<>(members);
    }
    public void setMembersListener(ListChangeListener<User> listener)
    {
        this.members.addListener(listener);
    }
}
