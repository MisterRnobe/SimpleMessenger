package client.window.main.dialog.controllers;

import client.application.ApplicationBank;
import client.application.DialogBean;
import client.application.Listener;
import common.objects.Message;
import common.objects.User;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;

import java.io.IOException;
import java.util.Date;

public class DialogWrapper extends AbstractWrapper {

    public DialogWrapper(int dialogId) throws IOException {
        super(dialogId);
        bindDialog();
    }

    private void bindDialog()
    {
        DialogBean dialogBean = ApplicationBank.getInstance().getDialogById(dialogId);
        dialogController.getTitle().textProperty().bind(dialogBean.titleProperty());
        bindMessages(dialogBean);

        String partner = dialogBean.getPartners().get(0);
        ApplicationBank.getInstance().addUserStatusListener(new UserStatusListener(), partner);

    }

    private class UserStatusListener implements Listener<User>
    {

        @Override
        public void onHandle(User user) {
            Platform.runLater(()-> {
                if (user.isOnline()) {
                    dialogController.getInfo().setText("В сети");
                } else {
                    Date d = new Date(user.getLastOnline());
                    dialogController.getInfo().setText(String.format("Был в сети %tc", d));
                }
            });

        }
    }
}
