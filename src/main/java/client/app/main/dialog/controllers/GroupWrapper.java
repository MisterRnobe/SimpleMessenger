package client.app.main.dialog.controllers;

import client.utils.ApplicationBank;
import client.utils.DialogBean;
import client.app.main.dialog.info.GroupInfoController;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;

import java.io.IOException;

public class GroupWrapper extends AbstractDialogWrapper {
    public GroupWrapper(int dialogId) throws IOException {
        super(dialogId);
        init();
    }
    private void init()
    {
        DialogBean dialogBean = ApplicationBank.getInstance().getDialogById(dialogId);
        dialogBean.users().addListener((ListChangeListener<String>) c -> {
            int newSize = c.getList().size();
            Platform.runLater(()->dialogController.getInfo().setText(newSize + " человек(а)"));
        });
        dialogController.getInfo().setText(dialogBean.users().size() + " человек(а)");
        dialogController.getTitle().textProperty().bind(dialogBean.titleProperty());
        bindMessages(dialogBean);
        dialogController.setInfoWindow(()->GroupInfoController.create(dialogId));

    }

}
