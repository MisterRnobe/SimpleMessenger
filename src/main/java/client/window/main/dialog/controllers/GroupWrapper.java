package client.window.main.dialog.controllers;

import client.application.ApplicationBank;
import client.application.DialogBean;

import java.io.IOException;

public class GroupWrapper extends AbstractWrapper {
    public GroupWrapper(int dialogId) throws IOException {
        super(dialogId);
        init();
    }
    private void init()
    {
        DialogBean dialogBean = ApplicationBank.getInstance().getDialogById(dialogId);
        dialogController.getInfo().setText(dialogBean.getPartners().size()+1 +" человек(а)");
        dialogController.getTitle().setText(dialogBean.titleProperty().getValue());
        bindMessages(dialogBean);
    }

}
