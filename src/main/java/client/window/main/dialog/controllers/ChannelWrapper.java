package client.window.main.dialog.controllers;

import client.application.ApplicationBank;
import client.application.DialogBean;

import java.io.IOException;

public class ChannelWrapper extends AbstractWrapper {
    public ChannelWrapper(int dialogId) throws IOException {
        super(dialogId);
        init();
    }
    private void init()
    {
        DialogBean dialogBean = ApplicationBank.getInstance().getDialogById(dialogId);
        dialogController.getTitle().setText(dialogBean.titleProperty().getValue());
        dialogController.getInfo().setText("Читают: "+ dialogBean.getPartners().size()+1 +" человек(а)");
        bindMessages(dialogBean);
        if (!dialogBean.creator.equalsIgnoreCase(ApplicationBank.getInstance().getLogin()))
        {
            dialogController.getMessageWindow().setBottom(null);
        }
    }
}
