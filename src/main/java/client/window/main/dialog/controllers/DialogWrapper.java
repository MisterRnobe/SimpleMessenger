package client.window.main.dialog.controllers;

import client.application.ApplicationBank;
import client.application.DialogBean;
import client.application.Listener;
import client.network.queries.GetUserStatusQuery;
import common.objects.Message;
import common.objects.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Date;

public class DialogWrapper extends AbstractWrapper {
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
        ApplicationBank.getInstance().addUserStatusListener(new UserStatusListener(), partner);


        dialogController.getMessageWindow().parentProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null)
            {
                System.out.println("Запускаю поток!");
                Thread subThread = new Thread(new SendQuery());
                subThread.start();
            }
        });
    }
    private class SendQuery implements Runnable
    {
        @Override
        public void run() {
            while (getRoot().getParent() != null)
            {
                try {
                    GetUserStatusQuery.sendQuery(partner);
                    Thread.sleep(10000);
                } catch (IOException|InterruptedException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("Я закончил работать");
        }
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
