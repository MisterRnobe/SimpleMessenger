package client.app.main.dialog;

import client.controllers.dialog.info.GroupInfoController;
import client.suppliers.AbstractDialogBean;
import client.suppliers.GroupBean;

import java.io.IOException;

class GroupWrapper extends AbstractDialogWrapper<GroupBean> {
    GroupWrapper(AbstractDialogBean dialogBean) throws IOException {
        super((GroupBean) dialogBean);
        init();
    }
    private void init()
    {
        /*dialogBean.userDBS().addListener((ListChangeListener<String>) c -> {
            int newSize = c.getList().size();
            Platform.runLater(()->dialogController.getInfo().setText(newSize + " человек(а)"));
        });*/
        dialogController.getInfo().setText(dialogBean.membersCount().getValue() + " человек(а)");
        dialogController.getTitle().textProperty().bind(dialogBean.title());
        bindMessages(dialogBean);
        dialogController.setInfoWindow(()->GroupInfoController.create(dialogBean.dialogId));

    }

}
