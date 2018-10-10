package client.app.main.dialog;

import client.utils.ApplicationBank;
import client.utils.DialogBean;

import java.io.IOException;

public class ChannelWrapper extends AbstractDialogWrapper {
    public ChannelWrapper(int dialogId) throws IOException {
        super(dialogId);
        init();
    }
    private void init()
    {
        DialogBean dialogBean = ApplicationBank.getInstance().getDialogById(dialogId);
        dialogController.getTitle().setText(dialogBean.titleProperty().getValue());
        dialogController.getInfo().setText("Читают: "+ dialogBean.getPartners().size()+" человек(а)");
        bindMessages(dialogBean);
        if (!dialogBean.creator.equalsIgnoreCase(ApplicationBank.getInstance().getLogin()))
        {
            dialogController.getMessageWindow().setBottom(null);
        }
    }
}
