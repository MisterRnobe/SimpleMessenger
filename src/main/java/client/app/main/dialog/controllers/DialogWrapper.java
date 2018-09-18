package client.app.main.dialog.controllers;

import client.utils.ApplicationBank;
import client.utils.DialogBean;
import client.utils.Listener;
import client.network.queries.GetUserStatusQuery;
import common.objects.User;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Date;

public class DialogWrapper extends AbstractDialogWrapper {
    private String partner;

    public DialogWrapper(int dialogId) throws IOException {
        super(dialogId);
        bindDialog();
    }

    private void bindDialog()
    {
        DialogBean dialogBean = ApplicationBank.getInstance().getDialogById(dialogId);
        dialogController.getTitle().textProperty().bind(dialogBean.titleProperty());
        bindMessages(dialogBean);

        partner = dialogBean.getPartners().get(0);

        try {
            GetUserStatusQuery.hookToUser(partner, user -> Platform.runLater(()-> {
                if (user.isOnline()) {
                    dialogController.getInfo().setText("В сети");
                } else {
                    Date d = new Date(user.getLastOnline());
                    dialogController.getInfo().setText(String.format("Был в сети %tc", d));
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
