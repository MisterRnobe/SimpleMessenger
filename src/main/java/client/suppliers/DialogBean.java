package client.suppliers;

import common.objects.Message;
import common.objects.User;
import common.objects.requests.DialogTypes;

public class DialogBean extends AbstractDialogBean{
    public final User partner;

    protected DialogBean(int dialogId, Message lastMessage, User partner, int unread) {
        super(DialogTypes.DIALOG, dialogId);
        this.lastMessage.set(lastMessage);
        this.partner = partner;
        this.unreadCount.set(unread);
        title.set(partner.getName());
        avatarPath.set(partner.getAvatarPath());
    }
}
