package common.objects;

public class GroupInfo extends DialogMarker{

    private String groupName;
    private String creator;

    private String avatarPath;
    private int usersCount;


    public String getGroupName() {
        return groupName;
    }

    public GroupInfo setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public GroupInfo setCreator(String creator) {
        this.creator = creator;
        return this;
    }


    public String getAvatarPath() {
        return avatarPath;
    }

    public GroupInfo setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
        return this;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public GroupInfo setUsersCount(int usersCount) {
        this.usersCount = usersCount;
        return this;
    }
}
