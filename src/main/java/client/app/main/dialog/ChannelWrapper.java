package client.app.main.dialog;

import client.controllers.dialog.info.GroupInfoController;
import client.suppliers.AbstractDialogBean;
import client.suppliers.GroupBean;
import client.suppliers.UserSupplier;

import java.io.IOException;

class ChannelWrapper extends AbstractDialogWrapper<GroupBean> {
    ChannelWrapper(AbstractDialogBean dialogBean) throws IOException {
        super((GroupBean) dialogBean);
        init();
    }

    private void init() {
        dialogController.getTitle().setText(dialogBean.title().getValue());
        dialogController.getInfo().setText("Читают: " + (dialogBean.membersCount().get() - 1) + " человек(а)");
        bindMessages(dialogBean);
        if (!dialogBean.creator.equalsIgnoreCase(UserSupplier.getInstance().getMyLogin())) {
            dialogController.getMessageWindow().setBottom(null);
        }
        dialogController.setInfoWindow(()-> GroupInfoController.create(dialogBean.dialogId));
    }
}
